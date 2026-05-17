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
import com.dcspa.prism.entity.RoleFonctionnalitePermission;
import com.dcspa.prism.entity.SousPrefecture;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AdminDashboardService adminDashboardService;

    // Charge l’utilisateur Spring Security à partir du nom de connexion.
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));
        return toAuthUser(user);
    }

    // Même chargement que loadUserByUsername, typé AuthUser (permissions incluses).
    @Transactional(readOnly = true)
    public AuthUser loadUserWithPermissions(String username) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));
        return toAuthUser(user);
    }

    // Vérifie mot de passe et compte actif, puis émet la réponse avec JWT et rôles.
    /** Un seul chargement utilisateur (graphe rôles/permissions) dans la même transaction lecture. */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
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
        List<String> roleCodes = user.getRoles().stream()
                .map(AppRole::getCodeRole)
                .collect(Collectors.toList());
        Map<String, Object> scope = adminDashboardService.buildSummary(authUser);
        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(authUser.getUserId())
                .username(authUser.getUsername())
                .email(user.getEmail())
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
                .build();
    }

    // Corps JSON pour GET /api/auth/me.
    @Transactional(readOnly = true)
    public Map<String, Object> buildAuthenticatedUserPayload(AuthUser user) {
        AppUser appUser = appUserRepository.findByUsername(user.getUsername()).orElse(null);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("userId", user.getUserId());
        payload.put("username", user.getUsername());
        payload.put("email", appUser != null ? appUser.getEmail() : null);
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
        Map<String, Object> scope = adminDashboardService.buildSummary(user);
        payload.put("nationalView", scope.get("nationalView"));
        payload.put("scopeMode", scope.get("scopeMode"));
        payload.put("scopeLabel", scope.get("scopeLabel"));
        return payload;
    }

    // Construit le principal avec la liste des permissions fonctionnelles.
    private AuthUser toAuthUser(AppUser user) {
        List<String> permissions = new ArrayList<>();
        for (AppRole role : user.getRoles()) {
            for (RoleFonctionnalitePermission rfp : role.getRoleFonctionnalitePermissions()) {
                String key = rfp.getFonctionnalite().getCodeFonctionnalite() + ":"
                        + rfp.getPermission().getCodePermission();
                if (!permissions.contains(key)) {
                    permissions.add(key);
                }
            }
        }
        return new AuthUser(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                Boolean.TRUE.equals(user.getActif()),
                permissions,
                roleCodes(user),
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
