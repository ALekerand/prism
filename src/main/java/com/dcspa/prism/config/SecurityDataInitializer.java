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

@Component
@RequiredArgsConstructor
public class SecurityDataInitializer {

    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final FonctionnaliteRepository fonctionnaliteRepository;
    private final PermissionRepository permissionRepository;
    private final RoleFonctionnalitePermissionRepository roleFonctionnalitePermissionRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    @Transactional
    public void initSecurityData() {
        Permission creer = getOrCreatePermission("CREER", "Créer");
        Permission lire = getOrCreatePermission("LIRE", "Lire");
        Permission modifier = getOrCreatePermission("MODIFIER", "Modifier");
        Permission supprimer = getOrCreatePermission("SUPPRIMER", "Supprimer");
        Permission valider = getOrCreatePermission("VALIDER", "Valider");
        Permission exporter = getOrCreatePermission("EXPORTER", "Exporter");

        Fonctionnalite campagnes = getOrCreateFonctionnalite("CAMPAGNE", "Gestion des campagnes", "Campagnes");
        Fonctionnalite centres = getOrCreateFonctionnalite("CENTRE", "Gestion des centres", "Centres");
        Fonctionnalite alpha = getOrCreateFonctionnalite("ALPHA", "Gestion des dispositifs Alpha", "Alpha");
        Fonctionnalite personnel = getOrCreateFonctionnalite("PERSONNEL", "Gestion du personnel", "Personnel");
        Fonctionnalite utilisateurs = getOrCreateFonctionnalite("UTILISATEUR", "Gestion des utilisateurs", "Sécurité");

        AppRole admin = getOrCreateRole("ADMIN", "Administrateur", "Accès complet à l'application");
        grant(admin, campagnes, creer, lire, modifier, supprimer, valider, exporter);
        grant(admin, centres, creer, lire, modifier, supprimer, valider, exporter);
        grant(admin, alpha, creer, lire, modifier, supprimer, valider, exporter);
        grant(admin, personnel, creer, lire, modifier, supprimer, valider, exporter);
        grant(admin, utilisateurs, creer, lire, modifier, supprimer, valider, exporter);

        AppRole lecteur = getOrCreateRole("LECTEUR", "Lecteur", "Consultation seule");
        grant(lecteur, campagnes, lire);
        grant(lecteur, centres, lire);
        grant(lecteur, alpha, lire);
        grant(lecteur, personnel, lire);

        if (!appUserRepository.existsByUsername("admin")) {
            AppUser userAdmin = new AppUser();
            userAdmin.setUsername("admin");
            userAdmin.setPasswordHash(passwordEncoder.encode("admin123"));
            userAdmin.setEmail("admin@prism.local");
            userAdmin.setActif(true);
            userAdmin.setRoles(Set.of(admin));
            appUserRepository.save(userAdmin);
        }
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
        for (Permission p : permissions) {
            if (roleFonctionnalitePermissionRepository.existsByRole_IdAndFonctionnalite_IdAndPermission_Id(
                    role.getId(), fonctionnalite.getId(), p.getId())) {
                continue;
            }
            RoleFonctionnalitePermission rfp = new RoleFonctionnalitePermission();
            rfp.setRole(role);
            rfp.setFonctionnalite(fonctionnalite);
            rfp.setPermission(p);
            roleFonctionnalitePermissionRepository.save(rfp);
            role.getRoleFonctionnalitePermissions().add(rfp);
        }
    }
}
