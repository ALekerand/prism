package com.dcspa.prism.controller.support;

import com.dcspa.prism.entity.ActivitesCentreWorkflowEntity;
import com.dcspa.prism.security.AuthUser;
import java.util.Arrays;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ActivitesCentreWorkflow {
	private static final String[] ADMIN_ROLES = { "ADMIN", "SUPER_ADMIN", "SUPER_ROOT" };

	private ActivitesCentreWorkflow() {
	}

	public static void initializeDraft(ActivitesCentreWorkflowEntity entity) {
		if (entity.getValideeCoordonnateur() == null) entity.setValideeCoordonnateur(false);
		if (entity.getValideeSuperviseur() == null) entity.setValideeSuperviseur(false);
		if (entity.getValideeCentrale() == null) entity.setValideeCentrale(false);
	}

	public static void ensureEditable(ActivitesCentreWorkflowEntity entity) {
		if (isStarted(entity)) {
			throw new IllegalArgumentException("Modification impossible : la donnée est déjà engagée dans le circuit de validation.");
		}
	}

	public static void putStatus(Map<String, Object> row, ActivitesCentreWorkflowEntity entity) {
		boolean coordonnateur = Boolean.TRUE.equals(entity.getValideeCoordonnateur());
		boolean superviseur = Boolean.TRUE.equals(entity.getValideeSuperviseur());
		boolean centrale = Boolean.TRUE.equals(entity.getValideeCentrale());
		row.put("valideeCoordonnateur", coordonnateur);
		row.put("valideeSuperviseur", superviseur);
		row.put("valideeCentrale", centrale);
		if (centrale) {
			row.put("niveauValidation", "Centrale");
			row.put("statutValidation", "Validé central");
		} else if (superviseur) {
			row.put("niveauValidation", "Superviseur DRENA");
			row.put("statutValidation", "Validé superviseur");
		} else if (coordonnateur) {
			row.put("niveauValidation", "Coordonnateur IEPP");
			row.put("statutValidation", "Validé coordonnateur");
		} else {
			row.put("niveauValidation", "Collecte IEPP");
			row.put("statutValidation", "En attente coordonnateur");
		}
	}

	public static ResponseEntity<?> validateCoordonnateur(ActivitesCentreWorkflowEntity entity, AuthUser user, String feature) {
		ResponseEntity<?> denied = requireValidator(user, feature, "COORDONNATEUR");
		if (denied != null) return denied;
		if (Boolean.TRUE.equals(entity.getValideeCoordonnateur())) {
			return badRequest("Cette donnée est déjà validée par le coordonnateur.");
		}
		entity.setValideeCoordonnateur(true);
		return null;
	}

	public static ResponseEntity<?> validateSuperviseur(ActivitesCentreWorkflowEntity entity, AuthUser user, String feature) {
		ResponseEntity<?> denied = requireValidator(user, feature, "SUPERVISEUR");
		if (denied != null) return denied;
		if (!Boolean.TRUE.equals(entity.getValideeCoordonnateur())) {
			return badRequest("Validation superviseur impossible : validation coordonnateur requise.");
		}
		if (Boolean.TRUE.equals(entity.getValideeSuperviseur())) {
			return badRequest("Cette donnée est déjà validée par le superviseur.");
		}
		entity.setValideeSuperviseur(true);
		return null;
	}

	public static ResponseEntity<?> validateCentrale(ActivitesCentreWorkflowEntity entity, AuthUser user, String feature) {
		ResponseEntity<?> denied = requireValidator(user, feature, "SUPERVISEUR_AENF", "DIRECTEUR");
		if (denied != null) return denied;
		if (!Boolean.TRUE.equals(entity.getValideeSuperviseur())) {
			return badRequest("Validation centrale impossible : validation superviseur requise.");
		}
		if (Boolean.TRUE.equals(entity.getValideeCentrale())) {
			return badRequest("Cette donnée est déjà validée au niveau central.");
		}
		entity.setValideeCentrale(true);
		return null;
	}

	private static boolean isStarted(ActivitesCentreWorkflowEntity entity) {
		return Boolean.TRUE.equals(entity.getValideeCoordonnateur())
				|| Boolean.TRUE.equals(entity.getValideeSuperviseur())
				|| Boolean.TRUE.equals(entity.getValideeCentrale());
	}

	private static ResponseEntity<?> requireValidator(AuthUser user, String feature, String... roles) {
		ResponseEntity<?> denied = PermissionGuard.require(user, feature, "VALIDER");
		if (denied != null) return denied;
		if (user != null && (user.hasAnyRole(roles) || user.hasAnyRole(ADMIN_ROLES))) {
			return null;
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(Map.of("message", "Accès refusé: rôle " + Arrays.toString(roles) + " requis"));
	}

	private static ResponseEntity<?> badRequest(String message) {
		return ResponseEntity.badRequest().body(Map.of("message", message));
	}
}
