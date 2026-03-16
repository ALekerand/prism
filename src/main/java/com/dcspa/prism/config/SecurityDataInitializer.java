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
        if (appUserRepository.count() > 0) {
            return;
        }

        Permission creer = savePermission("CREER", "Créer");
        Permission lire = savePermission("LIRE", "Lire");
        Permission modifier = savePermission("MODIFIER", "Modifier");
        Permission supprimer = savePermission("SUPPRIMER", "Supprimer");

        Fonctionnalite campagnes = saveFonctionnalite("CAMPAGNE", "Gestion des campagnes", "Campagnes");
        Fonctionnalite centres = saveFonctionnalite("CENTRE", "Gestion des centres", "Centres");
        Fonctionnalite alpha = saveFonctionnalite("ALPHA", "Gestion des dispositifs Alpha", "Alpha");
        Fonctionnalite personnel = saveFonctionnalite("PERSONNEL", "Gestion du personnel", "Personnel");
        Fonctionnalite utilisateurs = saveFonctionnalite("UTILISATEUR", "Gestion des utilisateurs", "Sécurité");

        AppRole admin = saveRole("ADMIN", "Administrateur", "Accès complet à l'application");
        grant(admin, campagnes, creer, lire, modifier, supprimer);
        grant(admin, centres, creer, lire, modifier, supprimer);
        grant(admin, alpha, creer, lire, modifier, supprimer);
        grant(admin, personnel, creer, lire, modifier, supprimer);
        grant(admin, utilisateurs, creer, lire, modifier, supprimer);

        AppRole lecteur = saveRole("LECTEUR", "Lecteur", "Consultation seule");
        grant(lecteur, campagnes, lire);
        grant(lecteur, centres, lire);
        grant(lecteur, alpha, lire);
        grant(lecteur, personnel, lire);

        AppUser userAdmin = new AppUser();
        userAdmin.setUsername("admin");
        userAdmin.setPasswordHash(passwordEncoder.encode("admin123"));
        userAdmin.setEmail("admin@prism.local");
        userAdmin.setActif(true);
        userAdmin.setRoles(Set.of(admin));
        appUserRepository.save(userAdmin);
    }

    private Permission savePermission(String code, String libelle) {
        Permission p = new Permission();
        p.setCodePermission(code);
        p.setLibellePermission(libelle);
        return permissionRepository.save(p);
    }

    private Fonctionnalite saveFonctionnalite(String code, String libelle, String module) {
        Fonctionnalite f = new Fonctionnalite();
        f.setCodeFonctionnalite(code);
        f.setLibelleFonctionnalite(libelle);
        f.setModule(module);
        return fonctionnaliteRepository.save(f);
    }

    private AppRole saveRole(String code, String libelle, String description) {
        AppRole r = new AppRole();
        r.setCodeRole(code);
        r.setLibelleRole(libelle);
        r.setDescriptionRole(description);
        return appRoleRepository.save(r);
    }

    private void grant(AppRole role, Fonctionnalite fonctionnalite, Permission... permissions) {
        for (Permission p : permissions) {
            RoleFonctionnalitePermission rfp = new RoleFonctionnalitePermission();
            rfp.setRole(role);
            rfp.setFonctionnalite(fonctionnalite);
            rfp.setPermission(p);
            roleFonctionnalitePermissionRepository.save(rfp);
            role.getRoleFonctionnalitePermissions().add(rfp);
        }
    }
}
