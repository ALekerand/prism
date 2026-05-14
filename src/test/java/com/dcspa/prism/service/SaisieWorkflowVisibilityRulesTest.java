package com.dcspa.prism.service;

import com.dcspa.prism.security.AuthUser;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SaisieWorkflowVisibilityRulesTest {

	private static AuthUser user(String username, String... roles) {
		return new AuthUser(1, username, "x", true, List.of(), List.of(roles), null, null, 10, null, null, null, null);
	}

	private static AuthUser superviseurDrena(int idDrena, Integer idIep) {
		return new AuthUser(
				2,
				"sup",
				"x",
				true,
				List.of(),
				List.of("SUPERVISEUR"),
				null,
				idDrena,
				idIep,
				null,
				null,
				null,
				null);
	}

	@Test
	void conseillerVoitSesSaisiesEtBrouillonSansProprietaire() {
		AuthUser c = user("alice", "CONSEILLER");
		Assertions.assertTrue(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(c, "SOUMIS", "alice", null, 10, null));
		Assertions.assertTrue(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(c, "BROUILLON", null, null, 10, null));
		Assertions.assertFalse(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(c, "SOUMIS", "bob", "bob", 10, null));
	}

	@Test
	void coordinateurVoitToutesLesLignesDeSonIepYComprisBrouillon() {
		AuthUser coord = user("coord", "COORDONNATEUR");
		Assertions.assertTrue(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(coord, "BROUILLON", null, null, 10, null));
		Assertions.assertTrue(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(coord, "SOUMIS", "alice", "alice", 10, null));
		Assertions.assertFalse(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(coord, "SOUMIS", "alice", "alice", 99, null));
	}

	@Test
	void adminVoitTout() {
		AuthUser admin = user("root", "ADMIN");
		Assertions.assertTrue(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(admin, "BROUILLON", null, null, null, null));
	}

	@Test
	void superviseurDrenaVoitHorsBrouillonMemeDrenaMemeSiAutreIep() {
		AuthUser sup = superviseurDrena(5, 10);
		Assertions.assertFalse(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(sup, "BROUILLON", "alice", null, 99, 5));
		Assertions.assertTrue(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(sup, "SOUMIS", "alice", "alice", 99, 5));
		Assertions.assertFalse(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(sup, "SOUMIS", "alice", "alice", 99, 6));
	}

	@Test
	void superviseurAenfVoitToutSansFiltreGeographique() {
		AuthUser aenf = new AuthUser(
				3,
				"aenf",
				"x",
				true,
				List.of(),
				List.of("SUPERVISEUR_AENF"),
				null,
				null,
				null,
				null,
				null,
				null,
				null);
		Assertions.assertTrue(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(aenf, "BROUILLON", "x", null, 999, 888));
	}
}
