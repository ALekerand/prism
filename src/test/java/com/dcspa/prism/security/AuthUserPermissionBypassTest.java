package com.dcspa.prism.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class AuthUserPermissionBypassTest {

	@Test
	void superRootHasEveryPermissionWithoutCatalog() {
		AuthUser user = new AuthUser(
				1, "admin", "x", true, List.of(), List.of("SUPER_ROOT"),
				null, null, null, null, null, null, null);
		assertTrue(user.hasPermission("CENTRES_ALPHA", "LIRE"));
		assertTrue(user.hasPermission("SAISIE_DONNEES", "VALIDER"));
	}

	@Test
	void conseillerRequiresExplicitPermission() {
		AuthUser user = new AuthUser(
				2, "c1", "x", true, List.of("POINTS_VISITES:LIRE"), List.of("CONSEILLER"),
				null, null, null, null, null, null, null);
		assertTrue(user.hasPermission("POINTS_VISITES", "LIRE"));
		assertFalse(user.hasPermission("ADMIN_UTILISATEURS", "LIRE"));
	}
}
