package com.dcspa.prism.service;

import com.dcspa.prism.dto.LoginRequest;
import com.dcspa.prism.dto.LoginResponse;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.entity.Commune;
import com.dcspa.prism.entity.Departement;
import com.dcspa.prism.entity.Drena;
import com.dcspa.prism.entity.Iep;
import com.dcspa.prism.entity.LocaliteDImplantation;
import com.dcspa.prism.entity.Region;
import com.dcspa.prism.entity.SousPrefecture;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.security.AuthUserCache;
import com.dcspa.prism.security.JwtUtil;
import com.dcspa.prism.security.PasswordRules;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final Set<String> RBAC_BYPASS_ROLES = Set.of("SUPER_ROOT", "SUPER_ADMIN", "ADMIN");

    private final AppUserRepository appUserRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AdminDashboardService adminDashboardService;
    private final AuthUserCache authUserCache;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));
        return toAuthUser(user);
    }

    @Transactional(readOnly = true)
    public AuthUser loadUserWithPermissions(String username) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));
        return toAuthUser(user);
    }

    /** Chargement utilisateur + rôles, sans graphe 300+ permissions pour les rôles admin. */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        long started = System.nanoTime();
        AppUser user = appUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + request.getUsername()));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Identifiants incorrects");
        }
        AuthUser authUser = toAuthUser(user);
        if (!authUser.isEnabled()) {
            throw new IllegalArgumentException("Compte désactivé");
        }
        String token = jwtUtil.generateToken(authUser.getUsername());
        authUserCache.put(authUser.getUsername(), authUser);
        List<String> roleCodes = user.getRoles().stream()
                .map(AppRole::getCodeRole)
                .collect(Collectors.toList());
        Map<String, Object> scope = adminDashboardService.buildScopeMeta(authUser);
        long elapsedMs = (System.nanoTime() - started) / 1_000_000L;
        log.info("Login {} en {} ms ({} rôles, {} permissions)", authUser.getUsername(), elapsedMs,
                roleCodes.size(), authUser.getPermissions().size());
        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(authUser.getUserId())
                .username(authUser.getUsername())
                .email(user.getEmail())
                .nom(user.getNom())
                .prenoms(user.getPrenoms())
                .dateNaissance(user.getDateNaissance())
                .lieuNaissance(user.getLieuNaissance())
                .roles(roleCodes)
                .permissions(authUser.getPermissions())
                .idRegion(authUser.getIdRegion())
                .idDrena(authUser.getIdDrena())
                .idIep(authUser.getIdIep())
                .idDepartement(authUser.getIdDepartement())
                .idSousPrefecture(authUser.getIdSousPrefecture())
                .idCommune(authUser.getIdCommune())
                .idLocalite(authUser.getIdLocalite())
                .region(ReferentielEnricher.toRef(user.getIdRegion()))
                .drena(ReferentielEnricher.toRef(user.getIdDrena()))
                .iep(ReferentielEnricher.toRef(user.getIdIep()))
                .departement(ReferentielEnricher.toRef(user.getIdDepartement()))
                .sousPrefecture(ReferentielEnricher.toRef(user.getIdSousPrefecture()))
                .commune(ReferentielEnricher.toRef(user.getIdCommune()))
                .localite(ReferentielEnricher.toRef(user.getIdLocalite()))
                .nationalView(Boolean.TRUE.equals(scope.get("nationalView")))
                .scopeMode(String.valueOf(scope.getOrDefault("scopeMode", "NATIONAL")))
                .scopeLabel(String.valueOf(scope.getOrDefault("scopeLabel", "Vue nationale")))
                .mustChangePassword(Boolean.TRUE.equals(user.getMustChangePassword()))
                .build();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> buildAuthenticatedUserPayload(AuthUser user) {
        AppUser appUser = appUserRepository.findByUsername(user.getUsername()).orElse(null);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("userId", user.getUserId());
        payload.put("username", user.getUsername());
        payload.put("email", appUser != null ? appUser.getEmail() : null);
        payload.put("nom", appUser != null ? appUser.getNom() : null);
        payload.put("prenoms", appUser != null ? appUser.getPrenoms() : null);
        payload.put("dateNaissance", appUser != null ? appUser.getDateNaissance() : null);
        payload.put("lieuNaissance", appUser != null ? appUser.getLieuNaissance() : null);
        payload.put("roles", appUser != null ? roleCodes(appUser) : List.of());
        payload.put("permissions", user.getPermissions());
        payload.put("idRegion", user.getIdRegion());
        payload.put("idDrena", user.getIdDrena());
        payload.put("idIep", user.getIdIep());
        payload.put("idDepartement", user.getIdDepartement());
        payload.put("idSousPrefecture", user.getIdSousPrefecture());
        payload.put("idCommune", user.getIdCommune());
        payload.put("idLocalite", user.getIdLocalite());
        payload.put("region", appUser != null ? ReferentielEnricher.toRef(appUser.getIdRegion()) : null);
        payload.put("drena", appUser != null ? ReferentielEnricher.toRef(appUser.getIdDrena()) : null);
        payload.put("iep", appUser != null ? ReferentielEnricher.toRef(appUser.getIdIep()) : null);
        payload.put("departement", appUser != null ? ReferentielEnricher.toRef(appUser.getIdDepartement()) : null);
        payload.put("sousPrefecture", appUser != null ? ReferentielEnricher.toRef(appUser.getIdSousPrefecture()) : null);
        payload.put("commune", appUser != null ? ReferentielEnricher.toRef(appUser.getIdCommune()) : null);
        payload.put("localite", appUser != null ? ReferentielEnricher.toRef(appUser.getIdLocalite()) : null);
        Map<String, Object> scope = adminDashboardService.buildScopeMeta(user);
        payload.put("nationalView", scope.get("nationalView"));
        payload.put("scopeMode", scope.get("scopeMode"));
        payload.put("scopeLabel", scope.get("scopeLabel"));
        payload.put(
                "mustChangePassword",
                appUser != null && Boolean.TRUE.equals(appUser.getMustChangePassword()));
        return payload;
    }

    @Transactional
    public Map<String, Object> updateOwnProfile(AuthUser principal, com.dcspa.prism.dto.UpdateProfileRequest request) {
        if (principal == null) {
            throw new IllegalArgumentException("Utilisateur non authentifié.");
        }
        if (request == null) {
            throw new IllegalArgumentException("Requête obligatoire");
        }
        AppUser user = appUserRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + principal.getUsername()));
        user.setEmail(trimToNull(request.getEmail()));
        user.setNom(trimToNull(request.getNom()));
        user.setPrenoms(trimToNull(request.getPrenoms()));
        user.setDateNaissance(request.getDateNaissance());
        user.setLieuNaissance(trimToNull(request.getLieuNaissance()));
        appUserRepository.save(user);
        authUserCache.invalidate(user.getUsername());
        return buildAuthenticatedUserPayload(principal);
    }

    @Transactional
    public void changeOwnPassword(AuthUser principal, String currentPassword, String newPassword) {
        if (principal == null) {
            throw new IllegalArgumentException("Utilisateur non authentifié.");
        }
        AppUser user = appUserRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + principal.getUsername()));
        if (currentPassword == null || !passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Mot de passe actuel incorrect.");
        }
        PasswordRules.requireNewPassword(newPassword);
        if (passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Le nouveau mot de passe doit être différent de l'actuel.");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);
        appUserRepository.save(user);
        authUserCache.invalidate(user.getUsername());
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private AuthUser toAuthUser(AppUser user) {
        List<String> roles = roleCodes(user);
        List<String> permissions = roles.stream().anyMatch(RBAC_BYPASS_ROLES::contains)
                ? List.of()
                : appUserRepository.findPermissionKeysByUsername(user.getUsername());
        return new AuthUser(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                Boolean.TRUE.equals(user.getActif()),
                permissions,
                roles,
                idOf(user.getIdRegion()),
                idOf(user.getIdDrena()),
                idOf(user.getIdIep()),
                idOf(user.getIdDepartement()),
                idOf(user.getIdSousPrefecture()),
                idOf(user.getIdCommune()),
                idOf(user.getIdLocalite())
        );
    }

    private List<String> roleCodes(AppUser user) {
        return user.getRoles().stream()
                .map(AppRole::getCodeRole)
                .collect(Collectors.toList());
    }

    private Integer idOf(Object entity) {
        if (entity == null) {
            return null;
        }
        return switch (entity) {
            case Region region -> region.getId();
            case Drena drena -> drena.getId();
            case Iep iep -> iep.getId();
            case Departement departement -> departement.getId();
            case SousPrefecture sousPrefecture -> sousPrefecture.getId();
            case Commune commune -> commune.getId();
            case LocaliteDImplantation localite -> localite.getId();
            default -> null;
        };
    }
}
