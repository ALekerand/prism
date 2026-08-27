package com.dcspa.prism.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.Fonctionnalite;
import com.dcspa.prism.entity.Permission;
import com.dcspa.prism.entity.RoleFonctionnalitePermission;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.FonctionnaliteRepository;
import com.dcspa.prism.repository.PermissionRepository;
import com.dcspa.prism.repository.RoleFonctionnalitePermissionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Vérifie que le conseiller reçoit les droits menu Apprenant (saisie) et le tableau de bord.
 */
@ExtendWith(MockitoExtension.class)
class ApprenantRbacInitializerTest {

	@Mock
	private AppRoleRepository appRoleRepository;
	@Mock
	private FonctionnaliteRepository fonctionnaliteRepository;
	@Mock
	private PermissionRepository permissionRepository;
	@Mock
	private RoleFonctionnalitePermissionRepository roleFonctionnalitePermissionRepository;

	@InjectMocks
	private ApprenantRbacInitializer initializer;

	private AppRole conseiller;
	private Permission lire;
	private Permission creer;

	@BeforeEach
	void setUp() {
		conseiller = role("CONSEILLER");
		lire = perm("LIRE");
		creer = perm("CREER");
		Permission modifier = perm("MODIFIER");

		whenRoleExists("CONSEILLER", conseiller);
		whenRoleExists("COORDONNATEUR", role("COORDONNATEUR"));
		whenRoleExists("SUPERVISEUR", role("SUPERVISEUR"));
		whenRoleExists("IEPP", role("IEPP"));
		whenRoleExists("SUPERVISEUR_AENF", role("SUPERVISEUR_AENF"));
		whenRoleExists("DIRECTEUR", role("DIRECTEUR"));

		when(permissionRepository.findByCodePermission("LIRE")).thenReturn(Optional.of(lire));
		when(permissionRepository.findByCodePermission("CREER")).thenReturn(Optional.of(creer));
		when(permissionRepository.findByCodePermission("MODIFIER")).thenReturn(Optional.of(modifier));

		when(fonctionnaliteRepository.findByCodeFonctionnalite("DASHBOARD"))
				.thenReturn(Optional.of(fn("DASHBOARD")));
		for (String code : List.of(
				"APPRENANT_EFFECTIF",
				"APPRENANT_ABANDON",
				"APPRENANT_PASSAGE",
				"APPRENANT_HANDICAP",
				"APPRENANT_CENTRES_PROMUS",
				"APPRENANT_REVERSE_FORMEL_SIE",
				"APPRENANT_ADMIS_CEPE",
				"APPRENANT_INTEGRES_FORMEL_CP",
				"APPRENANT_ADMIS_TEST_INTEGRATION_CP")) {
			when(fonctionnaliteRepository.findByCodeFonctionnalite(code)).thenReturn(Optional.of(fn(code)));
		}

		when(roleFonctionnalitePermissionRepository.existsByRole_IdAndFonctionnalite_IdAndPermission_Id(
				anyInt(), anyInt(), anyInt()))
				.thenReturn(false);
	}

	@Test
	void conseillerRecoitSaisieApprenantEtDashboard() {
		initializer.initApprenantMenuPermissions();

		ArgumentCaptor<RoleFonctionnalitePermission> captor = ArgumentCaptor.forClass(RoleFonctionnalitePermission.class);
		verify(roleFonctionnalitePermissionRepository, atLeastOnce()).save(captor.capture());

		List<RoleFonctionnalitePermission> saved = captor.getAllValues().stream()
				.filter(r -> r.getRole() == conseiller)
				.toList();

		assertThat(saved).anyMatch(r ->
				"APPRENANT_EFFECTIF".equals(r.getFonctionnalite().getCodeFonctionnalite())
						&& "CREER".equals(r.getPermission().getCodePermission()));
		assertThat(saved).anyMatch(r ->
				"DASHBOARD".equals(r.getFonctionnalite().getCodeFonctionnalite())
						&& "LIRE".equals(r.getPermission().getCodePermission()));
	}

	private static AppRole role(String code) {
		AppRole r = new AppRole();
		r.setId(code.hashCode());
		r.setCodeRole(code);
		return r;
	}

	private static Permission perm(String code) {
		Permission p = new Permission();
		p.setId(code.hashCode());
		p.setCodePermission(code);
		return p;
	}

	private static Fonctionnalite fn(String code) {
		Fonctionnalite f = new Fonctionnalite();
		f.setId(code.hashCode());
		f.setCodeFonctionnalite(code);
		return f;
	}

	private void whenRoleExists(String code, AppRole role) {
		when(appRoleRepository.findByCodeRole(code)).thenReturn(Optional.of(role));
	}
}
