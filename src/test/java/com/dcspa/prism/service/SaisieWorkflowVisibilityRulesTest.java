package com.dcspa.prism.service;

import com.dcspa.prism.security.AuthUser;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SaisieWorkflowVisibilityRulesTest {

	private static AuthUser user(String username, String... roles) {
		return new AuthUser(1, username, "x", true, List.of(), List.of(roles), null, null, 10, null, null, null, null);
	}

	@Test
	void conseillerVoitSesSaisiesEtBrouillonSansProprietaire() {
		AuthUser c = user("alice", "CONSEILLER");
		Assertions.assertTrue(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(c, "SOUMIS", "alice", null, 10));
		Assertions.assertTrue(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(c, "BROUILLON", null, null, 10));
		Assertions.assertFalse(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(c, "SOUMIS", "bob", "bob", 10));
	}

	@Test
	void coordinateurVoitSoumissionsMemeIepPasLesBrouillons() {
		AuthUser coord = user("coord", "COORDONNATEUR");
		Assertions.assertFalse(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(coord, "BROUILLON", null, null, 10));
		Assertions.assertTrue(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(coord, "SOUMIS", "alice", "alice", 10));
		Assertions.assertFalse(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(coord, "SOUMIS", "alice", "alice", 99));
	}

	@Test
	void adminVoitTout() {
		AuthUser admin = user("root", "ADMIN");
		Assertions.assertTrue(
				SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(admin, "BROUILLON", null, null, null));
	}
}
