package com.dcspa.prism.config;

import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.entity.Fonctionnalite;
import com.dcspa.prism.entity.Permission;
import com.dcspa.prism.entity.RoleFonctionnalitePermission;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.repository.FonctionnaliteRepository;
import com.dcspa.prism.repository.IeppRepository;
import com.dcspa.prism.repository.PermissionRepository;
import com.dcspa.prism.repository.RoleFonctionnalitePermissionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Droits et comptes de test « activités centre ». Les comptes commission ne sont pas gérés ici :
 * appliquer manuellement le script {@code db/commissions-1-4-users-seed.sql} si besoin.
 */
@Component
@RequiredArgsConstructor
public class ActivitesCentreRbacInitializer {

    private final AppRoleRepository appRoleRepository;
    private final AppUserRepository appUserRepository;
    private final FonctionnaliteRepository fonctionnaliteRepository;
    private final PermissionRepository permissionRepository;
    private final RoleFonctionnalitePermissionRepository roleFonctionnalitePermissionRepository;
    private final IeppRepository ieppRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String TEST_PASSWORD = "123456";

    private static final List<TestUserSeed> TEST_USERS = List.of(
            new TestUserSeed("conseiller_test", "conseiller.test@prism.local", "CONSEILLER"),
            new TestUserSeed("coordonnateur_test", "coordonnateur.test@prism.local", "COORDONNATEUR"),
            new TestUserSeed("iepp_test", "iepp.test@prism.local", "IEPP"),
            new TestUserSeed("superviseur_test", "superviseur.test@prism.local", "SUPERVISEUR"),
            new TestUserSeed("superviseur_aenf_test", "superviseur.aenf.test@prism.local", "SUPERVISEUR_AENF"));

    @EventListener(ApplicationReadyEvent.class)
    @Order(30)
    @Transactional
    public void initActivitesCentrePermissions() {
        Permission creer = getOrCreatePermission("CREER", "Créer");
        Permission lire = getOrCreatePermission("LIRE", "Lire");
        Permission modifier = getOrCreatePermission("MODIFIER", "Modifier");
        Permission valider = getOrCreatePermission("VALIDER", "Valider");

        Fonctionnalite pointsVisites = getOrCreateFonctionnalite("POINTS_VISITES", "Points des visites", "Activités centre");
        Fonctionnalite validationCoordonnateur = getOrCreateFonctionnalite("VALIDATION_VISITES_CONSEILLER", "Validation des visites conseiller", "Activités centre");
        Fonctionnalite suiviConseiller = getOrCreateFonctionnalite("SUIVI_CONSEILLER", "Suivi du conseiller", "Activités centre");
        Fonctionnalite suiviSuperviseur = getOrCreateFonctionnalite("SUIVI_SUPERVISEUR", "Suivi par le superviseur", "Activités centre");
        Fonctionnalite suiviIepp = getOrCreateFonctionnalite("SUIVI_IEPP", "Suivi par l'IEPP", "Activités centre");
        Fonctionnalite suiviCentrale = getOrCreateFonctionnalite("SUIVI_CENTRALE", "Suivi central AENF", "Activités centre");
        Fonctionnalite activitesPartenariat = getOrCreateFonctionnalite("ACTIVITES_CENTRE_PARTENARIAT", "Partenariat activités centre", "Activités centre");
        Fonctionnalite activitesPerformance = getOrCreateFonctionnalite("ACTIVITES_CENTRE_PERFORMANCE", "Performance activités centre", "Activités centre");
        Fonctionnalite activitesControle = getOrCreateFonctionnalite("ACTIVITES_CENTRE_CONTROLE", "Contrôle activités centre", "Activités centre");
        Fonctionnalite activitesEvaluation = getOrCreateFonctionnalite("ACTIVITES_CENTRE_EVALUATION", "Évaluation activités centre", "Activités centre");
        Fonctionnalite activitesInfos = getOrCreateFonctionnalite("ACTIVITES_CENTRE_INFOS", "Informations centres activités centre", "Activités centre");
        Fonctionnalite saisieDonnees = getOrCreateFonctionnalite("SAISIE_DONNEES", "Saisie des données", "Workflow");

        AppRole conseiller = getOrCreateRole("CONSEILLER", "Conseiller", "Niveau 1");
        AppRole coordonnateur = getOrCreateRole("COORDONNATEUR", "Coordonnateur", "Niveau 2");
        AppRole superviseur = getOrCreateRole("SUPERVISEUR", "Superviseur", "Niveau 3");
        AppRole iepp = getOrCreateRole("IEPP", "IEPP", "Inspection de l'enseignement primaire et préscolaire");
        AppRole superviseurAenf = getOrCreateRole("SUPERVISEUR_AENF", "Superviseur AENF", "Supervision centrale AENF");
        AppRole directeur = getOrCreateRole("DIRECTEUR", "Directeur", "Direction centrale");
        AppRole admin = getOrCreateRole("ADMIN", "Administrateur", "Accès complet à l'application");
        AppRole superAdmin = getOrCreateRole("SUPER_ADMIN", "Super admin", "Niveau 7");
        AppRole superRoot = getOrCreateRole("SUPER_ROOT", "Super Root", "Accès total à toutes les fonctionnalités");

        grant(conseiller, pointsVisites, lire, creer, modifier);
        grant(conseiller, suiviConseiller, lire, modifier);
        grant(conseiller, activitesPartenariat, lire, creer, modifier);
        grant(conseiller, activitesPerformance, lire, creer, modifier);
        grant(conseiller, activitesControle, lire, creer, modifier);
        grant(conseiller, activitesEvaluation, lire, creer, modifier);
        grant(conseiller, activitesInfos, lire);
        grant(conseiller, saisieDonnees, lire, creer, modifier);
        grant(coordonnateur, validationCoordonnateur, lire, valider);
        grant(coordonnateur, activitesPartenariat, lire);
        grant(coordonnateur, activitesPerformance, lire, valider);
        grant(coordonnateur, activitesControle, lire, valider);
        grant(coordonnateur, activitesEvaluation, lire, valider);
        grant(coordonnateur, activitesInfos, lire);
        grant(coordonnateur, saisieDonnees, lire, valider);
        grant(superviseur, suiviSuperviseur, lire, creer, modifier, valider);
        grant(superviseur, activitesPartenariat, lire);
        grant(superviseur, activitesPerformance, lire, valider);
        grant(superviseur, activitesControle, lire, valider);
        grant(superviseur, activitesEvaluation, lire, valider);
        grant(superviseur, activitesInfos, lire);
        grant(superviseur, saisieDonnees, lire, valider);
        grant(iepp, suiviIepp, lire, creer, modifier, valider);
        grant(iepp, activitesPartenariat, lire);
        grant(iepp, activitesPerformance, lire);
        grant(iepp, activitesControle, lire);
        grant(iepp, activitesEvaluation, lire);
        grant(iepp, activitesInfos, lire);
        grant(iepp, saisieDonnees, lire);
        grant(superviseurAenf, suiviCentrale, lire);
        grant(superviseurAenf, activitesPartenariat, lire);
        grant(superviseurAenf, activitesPerformance, lire, valider);
        grant(superviseurAenf, activitesControle, lire, valider);
        grant(superviseurAenf, activitesEvaluation, lire, valider);
        grant(superviseurAenf, activitesInfos, lire);
        grant(superviseurAenf, saisieDonnees, lire, valider);
        grant(directeur, suiviCentrale, lire);
        grant(directeur, activitesPartenariat, lire);
        grant(directeur, activitesPerformance, lire, valider);
        grant(directeur, activitesControle, lire, valider);
        grant(directeur, activitesEvaluation, lire, valider);
        grant(directeur, activitesInfos, lire);
        grant(directeur, saisieDonnees, lire, valider);

        grantAllPermissionsToRole(admin);
        grantAllPermissionsToRole(superAdmin);
        grantAllPermissionsToRole(superRoot);

        for (TestUserSeed seed : TEST_USERS) {
            ensureTestUser(seed);
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

    private void ensureTestUser(TestUserSeed seed) {
        AppRole role = appRoleRepository.findByCodeRole(seed.roleCode())
                .orElseThrow(() -> new IllegalStateException("Rôle introuvable: " + seed.roleCode()));
        AppUser user = appUserRepository.findByUsername(seed.username()).orElseGet(AppUser::new);
        user.setUsername(seed.username());
        user.setEmail(seed.email());
        user.setPasswordHash(passwordEncoder.encode(TEST_PASSWORD));
        user.setActif(true);
        user.getRoles().clear();
        user.getRoles().add(role);
        attachDemoIepIfNeeded(user, seed.roleCode());
        appUserRepository.save(user);
    }

    private void attachDemoIepIfNeeded(AppUser user, String roleCode) {
        if (!demoIepRole(roleCode)) {
            return;
        }
        ieppRepository.findAll().stream().findFirst().ifPresent(user::setIdIep);
    }

    private static boolean demoIepRole(String roleCode) {
        return "CONSEILLER".equals(roleCode)
                || "COORDONNATEUR".equals(roleCode)
                || "SUPERVISEUR".equals(roleCode)
                || "IEPP".equals(roleCode);
    }

    private record TestUserSeed(String username, String email, String roleCode) {
    }
}
