package com.dcspa.prism.service;

import com.dcspa.prism.entity.Visite;
import com.dcspa.prism.security.AuthUser;
import java.util.Map;

/**
 * Visibilité des lignes « visite » en liste / détail pour le périmètre commission
 * (aligné sur {@link SaisieWorkflowVisibilityRules} + IEP / DRENA du centre Alpha).
 */
public final class VisiteWorkflowListRules {

	private VisiteWorkflowListRules() {
	}

	public static boolean isRowVisible(Visite visite, Map<String, Object> workflowRow, AuthUser user) {
		return isRowVisible(visite, workflowRow, user, null, null);
	}

	public static boolean isRowVisible(
			Visite visite,
			Map<String, Object> workflowRow,
			AuthUser user,
			Integer rowIep,
			Integer rowDrena) {
		if (user == null) {
			return false;
		}
		String statut = stringField(workflowRow, "workflowStatut");
		String proprietaire = stringField(workflowRow, "workflowProprietaire");
		String soumisPar = stringField(workflowRow, "workflowSoumisPar");
		Integer iep = rowIep != null ? rowIep : resolveDenormalizedIep(visite);
		if (!SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(
				user, statut, proprietaire, soumisPar, iep, rowDrena)) {
			return false;
		}
		// Brouillon sans propriétaire : visible IEP-wide dans les règles génériques,
		// mais un conseiller ne doit voir que les visites de ses propres centres.
		if (user.hasRole("CONSEILLER") && !ownsWorkflow(user, proprietaire, soumisPar)) {
			return centreEnregistrePar(visite, user.getUsername());
		}
		return true;
	}

	private static boolean ownsWorkflow(AuthUser user, String proprietaire, String soumisPar) {
		String me = user.getUsername();
		if (me == null || me.isBlank()) {
			return false;
		}
		return me.equals(proprietaire) || me.equals(soumisPar);
	}

	private static boolean centreEnregistrePar(Visite visite, String username) {
		if (visite == null || visite.getIdAlpha() == null || username == null || username.isBlank()) {
			return false;
		}
		try {
			var centre = visite.getIdAlpha().getCentre();
			String par = centre == null ? null : centre.getEnregistrePar();
			return par != null && username.trim().equals(par.trim());
		} catch (RuntimeException ignored) {
			return false;
		}
	}

	private static Integer resolveDenormalizedIep(Visite visite) {
		if (visite == null || visite.getIdAlpha() == null) {
			return null;
		}
		try {
			return visite.getIdAlpha().getIdIep();
		} catch (RuntimeException ignored) {
			return null;
		}
	}

	private static String stringField(Map<String, Object> row, String key) {
		if (row == null) {
			return null;
		}
		Object v = row.get(key);
		return v == null ? null : String.valueOf(v);
	}
}
