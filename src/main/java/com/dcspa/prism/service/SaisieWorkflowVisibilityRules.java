package com.dcspa.prism.service;

import com.dcspa.prism.security.AuthUser;
import java.util.Objects;

/**
 * Règles de visibilité des lignes de saisie (liste) pour le scénario commission :
 * conseiller : uniquement ses saisies ; coordonnateur / IEPP : lignes de son IEP (tous statuts) ;
 * superviseur DRENA : lignes des IEP de sa DRENA (hors brouillon) ; niveau national : pas de filtre géographique.
 */
public final class SaisieWorkflowVisibilityRules {

	private SaisieWorkflowVisibilityRules() {
	}

	public static boolean rowVisibleInCommissionList(
			AuthUser user,
			String workflowStatut,
			String proprietaire,
			String soumisPar,
			Integer rowIepId,
			Integer rowDrenaId) {
		if (user == null) {
			return false;
		}
		if (user.hasAnyRole("ADMIN", "SUPER_ADMIN", "SUPER_ROOT", "SUPERVISEUR_AENF", "DIRECTEUR")) {
			return true;
		}
		String st = workflowStatut == null || workflowStatut.isBlank() ? "BROUILLON" : workflowStatut;
		if (user.hasRole("CONSEILLER")) {
			String me = user.getUsername();
			if (Objects.equals(me, proprietaire) || Objects.equals(me, soumisPar)) {
				return true;
			}
			return "BROUILLON".equals(st) && proprietaire == null && soumisPar == null;
		}
		if (user.hasRole("COORDONNATEUR") || user.hasRole("IEPP")) {
			if (user.getIdIep() != null && rowIepId != null && !user.getIdIep().equals(rowIepId)) {
				return false;
			}
			return true;
		}
		if (user.hasRole("SUPERVISEUR")) {
			if (user.getIdDrena() != null && rowDrenaId != null) {
				if (!user.getIdDrena().equals(rowDrenaId)) {
					return false;
				}
			} else if (user.getIdIep() != null && rowIepId != null && !user.getIdIep().equals(rowIepId)) {
				return false;
			}
			if ("BROUILLON".equals(st)) {
				return false;
			}
			return true;
		}
		if (user.getIdIep() != null && rowIepId != null && !user.getIdIep().equals(rowIepId)) {
			return false;
		}
		if ("BROUILLON".equals(st)) {
			return false;
		}
		return true;
	}
}
