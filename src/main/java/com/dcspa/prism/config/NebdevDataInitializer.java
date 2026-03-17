package com.dcspa.prism.config;

import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.entity.Fonctionnalite;
import com.dcspa.prism.entity.Permission;
import com.dcspa.prism.entity.RoleFonctionnalitePermission;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.repository.FonctionnaliteRepository;
import com.dcspa.prism.repository.PermissionRepository;
import com.dcspa.prism.repository.RoleFonctionnalitePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Crée l'utilisateur <strong>nebdev</strong> avec le rôle SUPER_ROOT
 * et toutes les permissions sur toutes les fonctionnalités.
 * Mot de passe par défaut : <strong>nebdev123</strong>
 */
@Component
@RequiredArgsConstructor
public class NebdevDataInitializer {

    private static final String USERNAME = "nebdev";
    private static final String DEFAULT_PASSWORD = "nebdev123";
    private static final String ROLE_CODE = "SUPER_ROOT";
    private static final String ROLE_LIBELLE = "Super Root";
    private static final String ROLE_DESCRIPTION = "Accès total à toutes les fonctionnalités (CRUD partout)";

    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final FonctionnaliteRepository fonctionnaliteRepository;
    private final PermissionRepository permissionRepository;
    private final RoleFonctionnalitePermissionRepository roleFonctionnalitePermissionRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Order(2)
    @Transactional
    public void initNebdev() {
        if (appUserRepository.existsByUsername(USERNAME)) {
            return;
        }

        AppRole superRoot = appRoleRepository.findByCodeRole(ROLE_CODE)
                .orElseGet(this::createSuperRootRole);

        grantAllPermissionsToRole(superRoot);

        AppUser nebdev = new AppUser();
        nebdev.setUsername(USERNAME);
        nebdev.setPasswordHash(passwordEncoder.encode(DEFAULT_PASSWORD));
        nebdev.setEmail("nebdev@prism.local");
        nebdev.setActif(true);
        nebdev.setRoles(Set.of(superRoot));
        appUserRepository.save(nebdev);
    }

    private AppRole createSuperRootRole() {
        AppRole r = new AppRole();
        r.setCodeRole(ROLE_CODE);
        r.setLibelleRole(ROLE_LIBELLE);
        r.setDescriptionRole(ROLE_DESCRIPTION);
        return appRoleRepository.save(r);
    }

    private void grantAllPermissionsToRole(AppRole role) {
        for (Fonctionnalite f : fonctionnaliteRepository.findAll()) {
            for (Permission p : permissionRepository.findAll()) {
                if (roleFonctionnalitePermissionRepository.existsByRole_IdAndFonctionnalite_IdAndPermission_Id(
                        role.getId(), f.getId(), p.getId())) {
                    continue;
                }
                RoleFonctionnalitePermission rfp = new RoleFonctionnalitePermission();
                rfp.setRole(role);
                rfp.setFonctionnalite(f);
                rfp.setPermission(p);
                roleFonctionnalitePermissionRepository.save(rfp);
                role.getRoleFonctionnalitePermissions().add(rfp);
            }
        }
    }
}
