package com.dcspa.prism.service;

import com.dcspa.prism.security.AuthUser;
import java.util.Objects;

/**
 * Règles de visibilité des lignes de saisie (liste) pour le scénario commission :
 * conseiller : uniquement ses saisies ; coordonnateur : toutes les lignes de son IEP (tous statuts) ;
 * niveaux supérieurs : données hors brouillon dans le même IEP lorsque l’utilisateur a un {@code idIep}.
 */
public final class SaisieWorkflowVisibilityRules {

	private SaisieWorkflowVisibilityRules() {
	}

	public static boolean rowVisibleInCommissionList(
			AuthUser user,
			String workflowStatut,
			String proprietaire,
			String soumisPar,
			Integer rowIepId) {
		if (user == null) {
			return false;
		}
		if (user.hasAnyRole("ADMIN", "SUPER_ADMIN", "SUPER_ROOT")) {
			return true;
		}
		String st = workflowStatut == null || workflowStatut.isBlank() ? "BROUILLON" : workflowStatut;
		if (user.hasRole("CONSEILLER")) {
			String me = user.getUsername();
			if (Objects.equals(me, proprietaire) || Objects.equals(me, soumisPar)) {
				return true;
			}
			// Brouillon sans propriétaire en base (données historiques) : visible pour permettre la revendication.
			return "BROUILLON".equals(st) && proprietaire == null && soumisPar == null;
		}
		if (user.hasAnyRole("COORDONNATEUR", "SUPERVISEUR", "SUPERVISEUR_AENF", "DIRECTEUR", "IEPP")) {
			if (user.getIdIep() != null && rowIepId != null && !user.getIdIep().equals(rowIepId)) {
				return false;
			}
			if (user.hasRole("COORDONNATEUR")) {
				return true;
			}
			if ("BROUILLON".equals(st)) {
				return false;
			}
			return true;
		}
		return true;
	}
}
