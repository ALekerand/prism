package com.dcspa.prism.config;

import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.Fonctionnalite;
import com.dcspa.prism.entity.Permission;
import com.dcspa.prism.entity.RoleFonctionnalitePermission;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.FonctionnaliteRepository;
import com.dcspa.prism.repository.PermissionRepository;
import com.dcspa.prism.repository.RoleFonctionnalitePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ActivitesCentreRbacInitializer {

    private final AppRoleRepository appRoleRepository;
    private final FonctionnaliteRepository fonctionnaliteRepository;
    private final PermissionRepository permissionRepository;
    private final RoleFonctionnalitePermissionRepository roleFonctionnalitePermissionRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Order(30)
    @Transactional
    public void initActivitesCentrePermissions() {
        Permission creer = getOrCreatePermission("CREER", "Créer");
        Permission lire = getOrCreatePermission("LIRE", "Lire");
        Permission modifier = getOrCreatePermission("MODIFIER", "Modifier");

        Fonctionnalite pointsVisites = getOrCreateFonctionnalite("POINTS_VISITES", "Points des visites", "Activités centre");
        Fonctionnalite suiviConseiller = getOrCreateFonctionnalite("SUIVI_CONSEILLER", "Suivi du conseiller", "Activités centre");
        Fonctionnalite suiviSuperviseur = getOrCreateFonctionnalite("SUIVI_SUPERVISEUR", "Suivi par le superviseur", "Activités centre");
        Fonctionnalite suiviIepp = getOrCreateFonctionnalite("SUIVI_IEPP", "Suivi par l'IEPP", "Activités centre");

        AppRole conseiller = getOrCreateRole("CONSEILLER", "Conseiller", "Niveau 1");
        AppRole superviseur = getOrCreateRole("SUPERVISEUR", "Superviseur", "Niveau 3");
        AppRole iepp = getOrCreateRole("IEPP", "IEPP", "Inspection de l'enseignement primaire et préscolaire");
        AppRole admin = getOrCreateRole("ADMIN", "Administrateur", "Accès complet à l'application");
        AppRole superAdmin = getOrCreateRole("SUPER_ADMIN", "Super admin", "Niveau 7");
        AppRole superRoot = getOrCreateRole("SUPER_ROOT", "Super Root", "Accès total à toutes les fonctionnalités");

        grant(conseiller, pointsVisites, lire, creer, modifier);
        grant(conseiller, suiviConseiller, lire, modifier);
        grant(superviseur, suiviSuperviseur, lire, modifier);
        grant(iepp, suiviIepp, lire, modifier);

        grantAllPermissionsToRole(admin);
        grantAllPermissionsToRole(superAdmin);
        grantAllPermissionsToRole(superRoot);
    }

    private Permission getOrCreatePermission(String code, String libelle) {
        return permissionRepository.findByCodePermission(code).orElseGet(() -> {
            Permission p = new Permission();
            p.setCodePermission(code);
            p.setLibellePermission(libelle);
            return permissionRepository.save(p);
        });
    }

    private Fonctionnalite getOrCreateFonctionnalite(String code, String libelle, String module) {
        return fonctionnaliteRepository.findByCodeFonctionnalite(code).orElseGet(() -> {
            Fonctionnalite f = new Fonctionnalite();
            f.setCodeFonctionnalite(code);
            f.setLibelleFonctionnalite(libelle);
            f.setModule(module);
            return fonctionnaliteRepository.save(f);
        });
    }

    private AppRole getOrCreateRole(String code, String libelle, String description) {
        return appRoleRepository.findByCodeRole(code).orElseGet(() -> {
            AppRole r = new AppRole();
            r.setCodeRole(code);
            r.setLibelleRole(libelle);
            r.setDescriptionRole(description);
            return appRoleRepository.save(r);
        });
    }

    private void grant(AppRole role, Fonctionnalite fonctionnalite, Permission... permissions) {
        for (Permission permission : permissions) {
            if (roleFonctionnalitePermissionRepository.existsByRole_IdAndFonctionnalite_IdAndPermission_Id(
                    role.getId(), fonctionnalite.getId(), permission.getId())) {
                continue;
            }
            RoleFonctionnalitePermission rfp = new RoleFonctionnalitePermission();
            rfp.setRole(role);
            rfp.setFonctionnalite(fonctionnalite);
            rfp.setPermission(permission);
            roleFonctionnalitePermissionRepository.save(rfp);
        }
    }

    private void grantAllPermissionsToRole(AppRole role) {
        for (Fonctionnalite fonctionnalite : fonctionnaliteRepository.findAll()) {
            for (Permission permission : permissionRepository.findAll()) {
                grant(role, fonctionnalite, permission);
            }
        }
    }
}
