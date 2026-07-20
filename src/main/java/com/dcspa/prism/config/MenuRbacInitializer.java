package com.dcspa.prism.config;

import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.Fonctionnalite;
import com.dcspa.prism.entity.Permission;
import com.dcspa.prism.entity.RoleFonctionnalitePermission;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.FonctionnaliteRepository;
import com.dcspa.prism.repository.PermissionRepository;
import com.dcspa.prism.repository.RoleFonctionnalitePermissionRepository;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Catalogue des fonctionnalités aligné sur le menu PRISM (granularité mixte : sections + sous-menus sensibles).
 * Les rôles ADMIN / SUPER_ADMIN / SUPER_ROOT reçoivent automatiquement tous les droits sur ce catalogue.
 */
@Component
@RequiredArgsConstructor
public class MenuRbacInitializer {

	private record MenuFeatureSeed(String code, String libelle, String module) {}

	private static final List<MenuFeatureSeed> MENU_FEATURES = List.of(
			new MenuFeatureSeed("DASHBOARD", "Tableau de bord", "Accueil"),
			new MenuFeatureSeed("CENTRES_ALPHA", "Centres Alpha", "Centres"),
			new MenuFeatureSeed("CENTRES_CEC", "Centres CEC", "Centres"),
			new MenuFeatureSeed("CENTRES_CP", "Centres CP", "Centres"),
			new MenuFeatureSeed("CENTRES_SIE", "Centres SIE", "Centres"),
			new MenuFeatureSeed("PERSONNEL", "Personnel", "Ressources humaines"),
			new MenuFeatureSeed("PROMOTEUR", "Promoteurs", "Partenaires"),
			new MenuFeatureSeed("APPRENANT_EFFECTIF", "Effectif apprenant", "Apprenant"),
			new MenuFeatureSeed("APPRENANT_ABANDON", "Effectif abandon", "Apprenant"),
			new MenuFeatureSeed("APPRENANT_PASSAGE", "Effectif passage (Alpha)", "Apprenant"),
			new MenuFeatureSeed("APPRENANT_HANDICAP", "Effectif handicap", "Apprenant"),
			new MenuFeatureSeed("APPRENANT_COMPETENCES", "Compétences acquises (Alpha)", "Apprenant"),
			new MenuFeatureSeed("APPRENANT_PROMUS", "Effectif promu (SIE, CEC)", "Apprenant"),
			new MenuFeatureSeed("APPRENANT_REVERSE_FORMEL_SIE", "Effectif reversé formel (SIE)", "Apprenant"),
			new MenuFeatureSeed("APPRENANT_ADMIS_CEPE", "Effectif admis CEPE", "Apprenant"),
			new MenuFeatureSeed("APPRENANT_INTEGRES_FORMEL_CP", "Effectif intégré formel (CP)", "Apprenant"),
			new MenuFeatureSeed("APPRENANT_ADMIS_TEST_INTEGRATION_CP", "Effectif admis test intégration (CP)", "Apprenant"),
			new MenuFeatureSeed("PARAMETRAGE_GEOGRAPHIE", "Paramétrage — Géographie", "Paramétrage"),
			new MenuFeatureSeed("PARAMETRAGE_CENTRES_AUTORISATIONS", "Paramétrage — Centres & autorisations", "Paramétrage"),
			new MenuFeatureSeed("PARAMETRAGE_PEDAGOGIE", "Paramétrage — Pédagogie", "Paramétrage"),
			new MenuFeatureSeed("PARAMETRAGE_ACTIVITES_CENTRE", "Paramétrage — Activités centre", "Paramétrage"),
			new MenuFeatureSeed("PARAMETRAGE_DOCUMENTS", "Paramétrage — Documents", "Paramétrage"),
			new MenuFeatureSeed("PARAMETRAGE_AUTRES", "Paramétrage — Autres", "Paramétrage"),
			new MenuFeatureSeed("ADMIN_UTILISATEURS", "Administration — Utilisateurs", "Administration"),
			new MenuFeatureSeed("ADMIN_ACTEURS", "Administration — Acteurs (rôles)", "Administration"),
			new MenuFeatureSeed("ADMIN_ROLE_PERMISSIONS", "Administration — Rôle permission", "Administration"));

	private static final List<String> ADMIN_ROLE_CODES = List.of("SUPER_ROOT", "SUPER_ADMIN", "ADMIN");

	private final FonctionnaliteRepository fonctionnaliteRepository;
	private final PermissionRepository permissionRepository;
	private final AppRoleRepository appRoleRepository;
	private final RoleFonctionnalitePermissionRepository roleFonctionnalitePermissionRepository;

	@EventListener(ApplicationReadyEvent.class)
	@Order(15)
	@Transactional
	public void initMenuFonctionnalites() {
		for (MenuFeatureSeed seed : MENU_FEATURES) {
			getOrCreateFonctionnalite(seed.code(), seed.libelle(), seed.module());
		}
		grantMenuCatalogToAdminRoles();
	}

	private void grantMenuCatalogToAdminRoles() {
		List<Permission> permissions = permissionRepository.findAll();
		for (String roleCode : ADMIN_ROLE_CODES) {
			appRoleRepository.findByCodeRole(roleCode).ifPresent(role -> {
				for (MenuFeatureSeed seed : MENU_FEATURES) {
					fonctionnaliteRepository.findByCodeFonctionnalite(seed.code()).ifPresent(fn -> {
						for (Permission perm : permissions) {
							grantIfMissing(role, fn, perm);
						}
					});
				}
			});
		}
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

	private void grantIfMissing(AppRole role, Fonctionnalite fonctionnalite, Permission permission) {
		if (roleFonctionnalitePermissionRepository.existsByRole_IdAndFonctionnalite_IdAndPermission_Id(
				role.getId(), fonctionnalite.getId(), permission.getId())) {
			return;
		}
		RoleFonctionnalitePermission rfp = new RoleFonctionnalitePermission();
		rfp.setRole(role);
		rfp.setFonctionnalite(fonctionnalite);
		rfp.setPermission(permission);
		roleFonctionnalitePermissionRepository.save(rfp);
	}
}
