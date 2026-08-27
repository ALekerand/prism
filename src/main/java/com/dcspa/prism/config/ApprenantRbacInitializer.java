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
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Droits menu « Apprenant » et tableau de bord pour les rôles opérationnels terrain
 * (conseiller, coordonnateur, superviseur, IEPP, centrale).
 */
@Component
@RequiredArgsConstructor
public class ApprenantRbacInitializer {

	private static final List<String> APPRENANT_MENU_CODES = List.of(
			"APPRENANT_EFFECTIF",
			"APPRENANT_ABANDON",
			"APPRENANT_PASSAGE",
			"APPRENANT_HANDICAP",
			"APPRENANT_CENTRES_PROMUS",
			"APPRENANT_REVERSE_FORMEL_SIE",
			"APPRENANT_ADMIS_CEPE",
			"APPRENANT_INTEGRES_FORMEL_CP",
			"APPRENANT_ADMIS_TEST_INTEGRATION_CP");

	private final AppRoleRepository appRoleRepository;
	private final FonctionnaliteRepository fonctionnaliteRepository;
	private final PermissionRepository permissionRepository;
	private final RoleFonctionnalitePermissionRepository roleFonctionnalitePermissionRepository;

	@EventListener(ApplicationReadyEvent.class)
	@Order(35)
	@Transactional
	public void initApprenantMenuPermissions() {
		Permission lire = requirePermission("LIRE");
		Permission creer = requirePermission("CREER");
		Permission modifier = requirePermission("MODIFIER");

		List<Fonctionnalite> apprenantFeatures = APPRENANT_MENU_CODES.stream()
				.map(this::requireFonctionnalite)
				.toList();
		Fonctionnalite dashboard = requireFonctionnalite("DASHBOARD");

		grantReadOnly(appRoleRepository.findByCodeRole("COORDONNATEUR").orElseThrow(), dashboard, lire);
		grantReadOnly(appRoleRepository.findByCodeRole("SUPERVISEUR").orElseThrow(), dashboard, lire);
		grantReadOnly(appRoleRepository.findByCodeRole("IEPP").orElseThrow(), dashboard, lire);
		grantReadOnly(appRoleRepository.findByCodeRole("SUPERVISEUR_AENF").orElseThrow(), dashboard, lire);
		grantReadOnly(appRoleRepository.findByCodeRole("DIRECTEUR").orElseThrow(), dashboard, lire);

		AppRole conseiller = appRoleRepository.findByCodeRole("CONSEILLER").orElseThrow();
		grantReadOnly(conseiller, dashboard, lire);
		for (Fonctionnalite fn : apprenantFeatures) {
			grant(conseiller, fn, lire, creer, modifier);
		}

		for (String roleCode : List.of("COORDONNATEUR", "SUPERVISEUR", "IEPP", "SUPERVISEUR_AENF", "DIRECTEUR")) {
			AppRole role = appRoleRepository.findByCodeRole(roleCode).orElseThrow();
			for (Fonctionnalite fn : apprenantFeatures) {
				grantReadOnly(role, fn, lire);
			}
		}
	}

	private Permission requirePermission(String code) {
		return permissionRepository.findByCodePermission(code)
				.orElseThrow(() -> new IllegalStateException("Permission introuvable: " + code));
	}

	private Fonctionnalite requireFonctionnalite(String code) {
		return fonctionnaliteRepository.findByCodeFonctionnalite(code)
				.orElseThrow(() -> new IllegalStateException("Fonctionnalité introuvable: " + code));
	}

	private void grantReadOnly(AppRole role, Fonctionnalite fonctionnalite, Permission lire) {
		grant(role, fonctionnalite, lire);
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
}
