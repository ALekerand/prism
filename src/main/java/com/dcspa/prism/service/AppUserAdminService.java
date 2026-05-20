package com.dcspa.prism.service;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.AppUserAdminResponse;
import com.dcspa.prism.dto.AppUserAdminUpsertRequest;
import com.dcspa.prism.dto.AppUserUpdateRolesRequest;
import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.repository.CommuneRepository;
import com.dcspa.prism.repository.DepartementRepository;
import com.dcspa.prism.repository.DrenaDepartementRepository;
import com.dcspa.prism.repository.DrenaRepository;
import com.dcspa.prism.repository.IeppRepository;
import com.dcspa.prism.repository.LocaliteDImplantationRepository;
import com.dcspa.prism.repository.RegionRepository;
import com.dcspa.prism.repository.SousPrefectureRepository;
import com.dcspa.prism.repository.spec.AppUserSpecifications;
import com.dcspa.prism.repository.spec.CentreCirconscriptionSpecifications;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.circonscription.CirconscriptionAttachement;
import com.dcspa.prism.service.circonscription.CirconscriptionLevel;
import com.dcspa.prism.service.pagination.PageableUtils;
import com.dcspa.prism.entity.Iep;
import com.dcspa.prism.entity.LocaliteDImplantation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserAdminService {

    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final RegionRepository regionRepository;
    private final DrenaRepository drenaRepository;
    private final IeppRepository ieppRepository;
    private final DepartementRepository departementRepository;
    private final SousPrefectureRepository sousPrefectureRepository;
    private final CommuneRepository communeRepository;
    private final LocaliteDImplantationRepository localiteDImplantationRepository;
    private final PasswordEncoder passwordEncoder;
    private final CirconscriptionResolver circonscriptionResolver;
    private final DrenaDepartementRepository drenaDepartementRepository;

    private static final Set<String> SCOPED_ROLE_CODES = Set.of(
            "CONSEILLER",
            "COORDONNATEUR",
            "SUPERVISEUR",
            "IEPP"
    );

    @Transactional(readOnly = true)
    public Page<AppUserAdminResponse> findAllWithRoles(
            Pageable pageable,
            String q,
            Integer roleId,
            Boolean actif,
            AuthUser authUser) {
        Pageable p = PageableUtils.cap(pageable);
        String qNorm = normalizeSearchText(q);
        Specification<AppUser> filter = AppUserSpecifications.forAdminSearch(qNorm, roleId, actif);
        Specification<AppUser> scope = CentreCirconscriptionSpecifications.forAppUser(
                circonscriptionResolver.resolve(authUser));
        Specification<AppUser> combined = scope == null
                ? filter
                : Specification.where(scope).and(filter);
        return appUserRepository.findAll(combined, p).map(this::toDto);
    }

    private static String normalizeSearchText(String q) {
        if (q == null) {
            return null;
        }
        String t = q.trim();
        return t.isEmpty() ? null : t;
    }

    @Transactional
    public void updateUserRoles(Integer userId, AppUserUpdateRolesRequest request, AuthUser authUser) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable: " + userId));
        assertUserVisible(user, authUser);

        List<Integer> roleIds = normalizeSingleRoleIds(request.getRoleIds());

        if (roleIds.isEmpty()) {
            user.setRoles(new HashSet<>());
            appUserRepository.save(user);
            return;
        }

        Set<AppRole> roles = new HashSet<>(appRoleRepository.findAllById(roleIds));
        if (roles.size() != roleIds.size()) {
            throw new IllegalArgumentException("Un ou plusieurs rôles sont introuvables");
        }

        user.setRoles(roles);
        appUserRepository.save(user);
    }

    @Transactional
    public AppUserAdminResponse createUser(AppUserAdminUpsertRequest request, AuthUser authUser) {
        if (request == null) throw new IllegalArgumentException("Requête obligatoire");
        String username = String.valueOf(request.getUsername() == null ? "" : request.getUsername()).trim();
        if (username.isBlank()) throw new IllegalArgumentException("username est obligatoire");
        if (appUserRepository.existsByUsername(username)) throw new IllegalArgumentException("username existe déjà");
        String password = String.valueOf(request.getPassword() == null ? "" : request.getPassword());
        if (password.isBlank()) throw new IllegalArgumentException("password est obligatoire");

        AppUser u = new AppUser();
        u.setUsername(username);
        u.setEmail(request.getEmail());
        u.setActif(Boolean.TRUE.equals(request.getActif()));
        u.setPasswordHash(passwordEncoder.encode(password));
        Set<AppRole> roles = resolveRoles(request.getRoleIds());
        u.setRoles(roles);
        applyScope(u, request);
        validateScopeForRoles(roles, u);
        assertUpsertWithinViewerScope(u, authUser);
        return toDto(appUserRepository.save(u));
    }

    @Transactional
    public AppUserAdminResponse updateUser(Integer id, AppUserAdminUpsertRequest request, AuthUser authUser) {
        if (request == null) throw new IllegalArgumentException("Requête obligatoire");
        AppUser u = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable: " + id));
        assertUserVisible(u, authUser);

        String username = String.valueOf(request.getUsername() == null ? "" : request.getUsername()).trim();
        if (username.isBlank()) throw new IllegalArgumentException("username est obligatoire");
        if (!Objects.equals(u.getUsername(), username) && appUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("username existe déjà");
        }

        u.setUsername(username);
        u.setEmail(request.getEmail());
        u.setActif(Boolean.TRUE.equals(request.getActif()));

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            u.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRoleIds() != null) {
            u.setRoles(resolveRoles(request.getRoleIds()));
        }

        applyScope(u, request);
        validateScopeForRoles(u.getRoles(), u);
        assertUpsertWithinViewerScope(u, authUser);

        return toDto(appUserRepository.save(u));
    }

    /**
     * Supprime l'utilisateur s'il existe.
     *
     * @return {@code true} si une ligne a été supprimée, {@code false} si aucun utilisateur avec cet id.
     */
    @Transactional
    public boolean deleteUserIfExists(Integer id, AuthUser authUser) {
        Optional<AppUser> existing = appUserRepository.findById(id);
        if (existing.isEmpty()) {
            return false;
        }
        AppUser user = existing.get();
        assertUserVisible(user, authUser);
        // Charger puis vider le côté propriétaire du ManyToMany pour supprimer les lignes de user_role
        // avant DELETE sur app_user (deleteById seul peut laisser le join et violer la FK MySQL).
        user.getRoles().clear();
        appUserRepository.delete(user);
        return true;
    }

    private Set<AppRole> resolveRoles(List<Integer> roleIds) {
        List<Integer> normalized = normalizeSingleRoleIds(roleIds);
        if (normalized.isEmpty()) return new HashSet<>();
        Set<AppRole> roles = new HashSet<>(appRoleRepository.findAllById(normalized));
        if (roles.size() != normalized.size()) {
            throw new IllegalArgumentException("Un ou plusieurs rôles sont introuvables");
        }
        return roles;
    }

    private List<Integer> normalizeSingleRoleIds(List<Integer> roleIds) {
        if (roleIds == null) return List.of();
        List<Integer> normalized = roleIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (normalized.size() > 1) {
            throw new IllegalArgumentException("Un utilisateur ne peut avoir qu'un seul rôle.");
        }
        return normalized;
    }

    private void applyScope(AppUser user, AppUserAdminUpsertRequest request) {
        user.setIdRegion(resolveOptional(regionRepository, request.getIdRegion(), "Région"));
        user.setIdDrena(resolveOptional(drenaRepository, request.getIdDrena(), "DRENA"));
        user.setIdIep(resolveOptional(ieppRepository, request.getIdIep(), "IEPP"));
        user.setIdDepartement(resolveOptional(departementRepository, request.getIdDepartement(), "Département"));
        user.setIdSousPrefecture(resolveOptional(sousPrefectureRepository, request.getIdSousPrefecture(), "Sous-préfecture"));
        user.setIdCommune(resolveOptional(communeRepository, request.getIdCommune(), "Commune"));
        user.setIdLocalite(resolveOptional(localiteDImplantationRepository, request.getIdLocalite(), "Localité"));
    }

    private <T> T resolveOptional(JpaRepository<T, Integer> repository, Integer id, String label) {
        if (id == null) {
            return null;
        }
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(label + " introuvable: " + id));
    }

    private void validateScopeForRoles(Set<AppRole> roles, AppUser user) {
        boolean needsScope = roles.stream()
                .map(AppRole::getCodeRole)
                .filter(Objects::nonNull)
                .anyMatch(SCOPED_ROLE_CODES::contains);
        if (!needsScope) {
            return;
        }
        if (!hasAnyScope(user)) {
            throw new IllegalArgumentException("Une circonscription est obligatoire pour les rôles CONSEILLER, COORDONNATEUR, SUPERVISEUR et IEPP.");
        }
        boolean isIepp = roles.stream().anyMatch(role -> "IEPP".equals(role.getCodeRole()));
        if (isIepp && user.getIdIep() == null) {
            throw new IllegalArgumentException("Le rôle IEPP doit être rattaché à une IEPP.");
        }
    }

    private boolean hasAnyScope(AppUser user) {
        return user.getIdRegion() != null
                || user.getIdDrena() != null
                || user.getIdIep() != null
                || user.getIdDepartement() != null
                || user.getIdSousPrefecture() != null
                || user.getIdCommune() != null
                || user.getIdLocalite() != null;
    }

    private void assertUserVisible(AppUser target, AuthUser viewer) {
        if (target == null || circonscriptionResolver.isNationalView(viewer)) {
            return;
        }
        Specification<AppUser> scope = CentreCirconscriptionSpecifications.forAppUser(
                circonscriptionResolver.resolve(viewer));
        if (scope == null) {
            return;
        }
        Specification<AppUser> byId = (root, query, cb) -> cb.equal(root.get("id"), target.getId());
        if (!appUserRepository.exists(scope.and(byId))) {
            throw new IllegalArgumentException("Utilisateur hors de votre circonscription.");
        }
    }

    private void assertUpsertWithinViewerScope(AppUser target, AuthUser viewer) {
        if (target == null || circonscriptionResolver.isNationalView(viewer)) {
            return;
        }
        CirconscriptionAttachement att = circonscriptionResolver.resolve(viewer);
        if (att.level() == CirconscriptionLevel.NONE) {
            return;
        }
        if (!userMatchesCirconscription(target, att)) {
            throw new IllegalArgumentException(
                    "La circonscription de l'utilisateur doit rester dans votre périmètre.");
        }
    }

    private boolean userMatchesCirconscription(AppUser user, CirconscriptionAttachement att) {
        return switch (att.level()) {
            case NONE -> true;
            case IEP -> user.getIdIep() != null && att.scopeId().equals(user.getIdIep().getId());
            case DRENA -> {
                if (user.getIdDrena() != null && att.scopeId().equals(user.getIdDrena().getId())) {
                    yield true;
                }
                if (user.getIdIep() == null) {
                    yield false;
                }
                yield ieppRepository.findById(user.getIdIep().getId())
                        .map(Iep::getIdDrena)
                        .filter(d -> d != null && att.scopeId().equals(d.getId()))
                        .isPresent();
            }
            case REGION -> userInRegion(user, att.scopeId());
        };
    }

    private boolean userInRegion(AppUser user, Integer regionId) {
        Integer direct = regionIdOfUser(user);
        if (direct != null) {
            return direct.equals(regionId);
        }
        if (user.getIdDrena() != null) {
            return drenaDepartementRepository.existsByIdDrena_IdAndIdDepartement_IdRegion_Id(
                    user.getIdDrena().getId(), regionId);
        }
        if (user.getIdIep() != null) {
            return ieppRepository.findById(user.getIdIep().getId())
                    .map(Iep::getIdDrena)
                    .filter(d -> d != null)
                    .map(d -> drenaDepartementRepository.existsByIdDrena_IdAndIdDepartement_IdRegion_Id(
                            d.getId(), regionId))
                    .orElse(false);
        }
        return false;
    }

    private Integer regionIdOfUser(AppUser user) {
        if (user.getIdRegion() != null) {
            return user.getIdRegion().getId();
        }
        if (user.getIdDepartement() != null && user.getIdDepartement().getIdRegion() != null) {
            return user.getIdDepartement().getIdRegion().getId();
        }
        if (user.getIdSousPrefecture() != null
                && user.getIdSousPrefecture().getIdDepartement() != null
                && user.getIdSousPrefecture().getIdDepartement().getIdRegion() != null) {
            return user.getIdSousPrefecture().getIdDepartement().getIdRegion().getId();
        }
        if (user.getIdLocalite() != null) {
            return regionIdOfLocalite(user.getIdLocalite());
        }
        return null;
    }

    private static Integer regionIdOfLocalite(LocaliteDImplantation loc) {
        if (loc.getIdSousPrefecture() == null || loc.getIdSousPrefecture().getIdDepartement() == null) {
            return null;
        }
        var dep = loc.getIdSousPrefecture().getIdDepartement();
        return dep.getIdRegion() != null ? dep.getIdRegion().getId() : null;
    }

    private Integer idOf(Map<String, Object> ref) {
        Object id = ref != null ? ref.get("id") : null;
        return id instanceof Number number ? number.intValue() : null;
    }

    private AppUserAdminResponse toDto(AppUser u) {
        List<Integer> roleIds = u.getRoles()
                .stream()
                .map(AppRole::getId)
                .collect(Collectors.toList());
        Map<String, Object> region = ReferentielEnricher.toRef(u.getIdRegion());
        Map<String, Object> drena = ReferentielEnricher.toRef(u.getIdDrena());
        Map<String, Object> iep = ReferentielEnricher.toRef(u.getIdIep());
        Map<String, Object> departement = ReferentielEnricher.toRef(u.getIdDepartement());
        Map<String, Object> sousPrefecture = ReferentielEnricher.toRef(u.getIdSousPrefecture());
        Map<String, Object> commune = ReferentielEnricher.toRef(u.getIdCommune());
        Map<String, Object> localite = ReferentielEnricher.toRef(u.getIdLocalite());

        return AppUserAdminResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .actif(u.getActif())
                .roleIds(roleIds)
                .idRegion(idOf(region))
                .idDrena(idOf(drena))
                .idIep(idOf(iep))
                .idDepartement(idOf(departement))
                .idSousPrefecture(idOf(sousPrefecture))
                .idCommune(idOf(commune))
                .idLocalite(idOf(localite))
                .region(region)
                .drena(drena)
                .iep(iep)
                .departement(departement)
                .sousPrefecture(sousPrefecture)
                .commune(commune)
                .localite(localite)
                .build();
    }
}

