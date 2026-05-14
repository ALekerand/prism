package com.dcspa.prism.service;

import com.dcspa.prism.entity.Alpha;
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
		if (user == null) {
			return false;
		}
		String statut = stringField(workflowRow, "workflowStatut");
		String proprietaire = stringField(workflowRow, "workflowProprietaire");
		String soumisPar = stringField(workflowRow, "workflowSoumisPar");
		Integer rowIep = resolveRowIepId(visite);
		Integer rowDrena = resolveRowDrenaId(visite);
		return SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(user, statut, proprietaire, soumisPar, rowIep, rowDrena);
	}

	private static Integer resolveRowIepId(Visite visite) {
		Alpha alpha = visite == null ? null : visite.getIdAlpha();
		if (alpha == null) {
			return null;
		}
		try {
			if (alpha.getCentre() != null && alpha.getCentre().getIdIep() != null) {
				return alpha.getCentre().getIdIep().getId();
			}
		} catch (RuntimeException ignored) {
			// liaison lazy non initialisée : repli sur la colonne dénormalisée
		}
		return alpha.getIdIep();
	}

	private static Integer resolveRowDrenaId(Visite visite) {
		Alpha alpha = visite == null ? null : visite.getIdAlpha();
		if (alpha == null) {
			return null;
		}
		try {
			if (alpha.getCentre() != null
					&& alpha.getCentre().getIdIep() != null
					&& alpha.getCentre().getIdIep().getIdDrena() != null) {
				return alpha.getCentre().getIdIep().getIdDrena().getId();
			}
		} catch (RuntimeException ignored) {
			// lazy non initialisé
		}
		return null;
	}

	private static String stringField(Map<String, Object> row, String key) {
		if (row == null) {
			return null;
		}
		Object v = row.get(key);
		return v == null ? null : String.valueOf(v);
	}
}
