package com.dcspa.prism.service;

import com.dcspa.prism.entity.SaisieWorkflow;
import com.dcspa.prism.entity.SaisieWorkflowStatus;
import com.dcspa.prism.security.AuthUser;
import java.util.Objects;

/**
 * Classe les lignes workflow en 3 onglets selon le rôle de l'utilisateur connecté.
 */
public final class SaisieWorkflowQueueTabResolver {

	private static final String[] ADMIN_ROLES = { "ADMIN", "SUPER_ADMIN", "SUPER_ROOT" };
	private static final String[] VALIDATOR_ROLES = {
			"COORDONNATEUR", "SUPERVISEUR", "SUPERVISEUR_AENF", "DIRECTEUR", "IEPP"
	};

	private SaisieWorkflowQueueTabResolver() {
	}

	public static SaisieWorkflowListTab resolve(SaisieWorkflow workflow, AuthUser user) {
		if (workflow == null || user == null) {
			return SaisieWorkflowListTab.EN_COURS;
		}
		if (user.hasRole("CONSEILLER") && concernsConseiller(workflow, user)) {
			return resolveConseillerTab(workflow, user);
		}
		if (actsAsValidator(user)) {
			return resolveValidatorTab(workflow, user);
		}
		if (user.hasRole("CONSEILLER")) {
			return resolveConseillerTab(workflow, user);
		}
		return SaisieWorkflowListTab.EN_COURS;
	}

	public static String tabLabel(SaisieWorkflowListTab tab, AuthUser user) {
		boolean conseillerView = user != null && user.hasRole("CONSEILLER") && !actsAsValidator(user);
		boolean superviseurView = user != null && user.hasRole("SUPERVISEUR") && !user.hasAnyRole(ADMIN_ROLES);
		return switch (tab) {
			case ACTION -> conseillerView ? "À soumettre" : (superviseurView ? "En attente" : "À valider");
			case EN_COURS -> conseillerView ? "En attente de validation" : "À venir";
			case TERMINE -> conseillerView ? "Validé" : (superviseurView ? "Traité" : "Déjà validé par moi");
			case RENVOYE -> "Renvoyé pour modification";
		};
	}

	private static boolean actsAsValidator(AuthUser user) {
		if (user.hasAnyRole(ADMIN_ROLES)) {
			return true;
		}
		return user.hasAnyRole(VALIDATOR_ROLES);
	}

	private static SaisieWorkflowListTab resolveConseillerTab(SaisieWorkflow workflow, AuthUser user) {
		if (!concernsConseiller(workflow, user)) {
			return SaisieWorkflowListTab.EN_COURS;
		}
		return switch (workflow.getStatut()) {
			case BROUILLON -> SaisieWorkflowListTab.ACTION;
			case RETOURNE -> SaisieWorkflowListTab.RENVOYE;
			case SOUMIS -> SaisieWorkflowListTab.EN_COURS;
			case VALIDEE_COORDONNATEUR, VALIDEE_SUPERVISEUR, VALIDEE_CENTRALE, REJETE -> SaisieWorkflowListTab.TERMINE;
		};
	}

	private static SaisieWorkflowListTab resolveValidatorTab(SaisieWorkflow workflow, AuthUser user) {
		String me = username(user);
		if (iValidatedAtMyLevel(workflow, me, user)) {
			return SaisieWorkflowListTab.TERMINE;
		}
		if (canValidateNow(workflow, user)) {
			return SaisieWorkflowListTab.ACTION;
		}
		if (workflow.getStatut() == SaisieWorkflowStatus.RETOURNE) {
			return SaisieWorkflowListTab.RENVOYE;
		}
		if (workflow.getStatut() == SaisieWorkflowStatus.BROUILLON) {
			return SaisieWorkflowListTab.EN_COURS;
		}
		return SaisieWorkflowListTab.EN_COURS;
	}

	private static boolean concernsConseiller(SaisieWorkflow workflow, AuthUser user) {
		String me = username(user);
		if (me == null) {
			return false;
		}
		if (Objects.equals(me, workflow.getProprietaire()) || Objects.equals(me, workflow.getSoumisPar())) {
			return true;
		}
		return workflow.getStatut() == SaisieWorkflowStatus.BROUILLON
				&& workflow.getProprietaire() == null
				&& workflow.getSoumisPar() == null;
	}

	private static boolean canValidateNow(SaisieWorkflow workflow, AuthUser user) {
		return switch (workflow.getStatut()) {
			case SOUMIS -> user.hasAnyRole("COORDONNATEUR", "IEPP") || user.hasAnyRole(ADMIN_ROLES);
			case VALIDEE_COORDONNATEUR ->
					user.hasRole("SUPERVISEUR") || user.hasAnyRole(ADMIN_ROLES);
			case VALIDEE_SUPERVISEUR ->
					user.hasAnyRole("SUPERVISEUR_AENF", "DIRECTEUR") || user.hasAnyRole(ADMIN_ROLES);
			default -> false;
		};
	}

	private static boolean iValidatedAtMyLevel(SaisieWorkflow workflow, String me, AuthUser user) {
		if (me == null) {
			return false;
		}
		if (Objects.equals(me, workflow.getValideCoordPar())
				|| Objects.equals(me, workflow.getValideSupPar())
				|| Objects.equals(me, workflow.getValideCentralPar())) {
			return true;
		}
		SaisieWorkflowStatus st = workflow.getStatut();
		if ((st == SaisieWorkflowStatus.REJETE || st == SaisieWorkflowStatus.RETOURNE)
				&& Objects.equals(me, workflow.getDecidePar())) {
			return true;
		}
		return false;
	}

	private static String username(AuthUser user) {
		return user == null || user.getUsername() == null ? null : user.getUsername().trim();
	}
}
