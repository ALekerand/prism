package com.dcspa.prism.service;

import com.dcspa.prism.entity.SaisieWorkflow;
import com.dcspa.prism.entity.SaisieWorkflowStatus;
import com.dcspa.prism.security.AuthUser;
import java.util.Set;

/**
 * Règles d’alerte tableau de bord pour les saisies {@link SaisieWorkflowStatus#RETOURNE}.
 */
public final class SaisieWorkflowReturnNotificationRules {

	private static final Set<String> SUPERIOR_ROLES = Set.of(
			"SUPERVISEUR",
			"SUPERVISEUR_AENF",
			"DIRECTEUR",
			"IEPP",
			"ADMIN",
			"SUPER_ADMIN",
			"SUPER_ROOT");

	private SaisieWorkflowReturnNotificationRules() {
	}

	public static boolean shouldNotify(AuthUser user, SaisieWorkflow workflow) {
		if (user == null || workflow == null || workflow.getStatut() != SaisieWorkflowStatus.RETOURNE) {
			return false;
		}
		if (isConseillerView(user)) {
			return SaisieWorkflowQueueTabResolver.resolve(workflow, user) == SaisieWorkflowListTab.RENVOYE;
		}
		if (user.hasRole("COORDONNATEUR") || user.hasRole("IEPP")) {
			return notifyCoordonnateur(workflow, user);
		}
		return false;
	}

	public static boolean shouldNotifyCoordonnateur(SaisieWorkflow workflow, AuthUser user, boolean decideParIsSuperior) {
		return notifyCoordonnateur(workflow, user) && decideParIsSuperior;
	}

	/** Conseiller pur (sans rôle validateur). */
	private static boolean isConseillerView(AuthUser user) {
		return user.hasRole("CONSEILLER")
				&& !user.hasAnyRole("COORDONNATEUR", "IEPP", "SUPERVISEUR", "SUPERVISEUR_AENF", "DIRECTEUR",
						"ADMIN", "SUPER_ADMIN", "SUPER_ROOT");
	}

	/**
	 * Coordonnateur : alerte seulement si un supérieur lui renvoie la saisie
	 * (pas quand il renvoie lui-même au conseiller, ni quand le supérieur renvoie au conseiller après validation coord.).
	 */
	static boolean notifyCoordonnateur(SaisieWorkflow workflow, AuthUser user) {
		String me = normalize(user.getUsername());
		String decidePar = normalize(workflow.getDecidePar());
		if (me != null && me.equals(decidePar)) {
			return false;
		}
		if (workflow.getValideCoordPar() != null) {
			return false;
		}
		return decidePar != null;
	}

	static boolean isSuperiorRoleCode(String roleCode) {
		return roleCode != null && SUPERIOR_ROLES.contains(roleCode);
	}

	private static String normalize(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}
}
