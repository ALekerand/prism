package com.dcspa.prism.service;

import java.util.Map;

/**
 * Métadonnées SQL natives pour les fiches effectif (alertes workflow).
 * Le schéma PRISM utilise souvent {@code id_effectif_debutNN} comme PK, pas {@code id_<table>}.
 */
public final class WorkflowEffectifSqlSupport {

	public enum CentreLink {
		/** {@code e.id_centre} → {@code alpha}. */
		ALPHA,
		CEC,
		CP,
		SIE,
		/** {@code e.id_centre} optionnel → {@code sie} (abandon / handicap SIE). */
		SIE_OPTIONAL,
		/** Pas de rattachement centre en BDD : périmètre géo non résolu (évite une 500). */
		NONE
	}

	public record EffectifSqlMeta(String table, String pkColumn, String codeColumn, CentreLink centreLink) {
	}

	private static final Map<String, EffectifSqlMeta> BY_PATH = Map.ofEntries(
			Map.entry("/api/effectif-alpha",
					new EffectifSqlMeta("effectif_alpha", "id_effectif_alpha", "code_effectif_alpha", CentreLink.ALPHA)),
			Map.entry("/api/effectif-abandon-alpha",
					new EffectifSqlMeta("effectif_abandon_alpha", "id_effectif_abandon_alpha", "code_effectif_abandon_alpha",
							CentreLink.ALPHA)),
			Map.entry("/api/effectif-passage-alpha",
					new EffectifSqlMeta("effectif_passage_alpha", "id_effectif_passage_alpha", "code_effectif_passage_alpha",
							CentreLink.ALPHA)),
			Map.entry("/api/effectif-situation-handicap-alpha",
					new EffectifSqlMeta("effectif_situation_handicap_alpha", "id_effectif_situation_handicap_alpha",
							"code_effectif_situation_handicap_alpha", CentreLink.ALPHA)),

			Map.entry("/api/effectif-cec",
					new EffectifSqlMeta("effectif_cec", "id_effectif_cec", "code_effectif_cec", CentreLink.CEC)),
			Map.entry("/api/effectif-abandon-cec",
					new EffectifSqlMeta("effectif_abandon_cec", "id_effectif_debut10", "code_effectif_abandon_cec", CentreLink.CEC)),
			Map.entry("/api/effectif-cepe-cec",
					new EffectifSqlMeta("effectif_cepe_cec", "id_effectif_debut12", "code_effectif_cepe_cec", CentreLink.CEC)),
			Map.entry("/api/effectif-promu-cec",
					new EffectifSqlMeta("effectif_promu_cec", "id_effectif_debut11", "code_effectif_promu_cec", CentreLink.CEC)),
			Map.entry("/api/effectif-situation-handicap-cec",
					new EffectifSqlMeta("effectif_situation_handicap_cec", "id_effectif_debut13",
							"code_effectif_situation_handicap_cec", CentreLink.NONE)),

			Map.entry("/api/effectif-cp",
					new EffectifSqlMeta("effectif_cp", "id_effectif_debut14", "code_effectif_cp", CentreLink.CP)),
			Map.entry("/api/effectif-abandon-cp",
					new EffectifSqlMeta("effectif_abandon_cp", "id_effectif_debut15", "code_effectif_abandon_cp", CentreLink.CP)),
			Map.entry("/api/effectif-cepe-cp",
					new EffectifSqlMeta("effectif_cepe_cp", "id_effectif_debut18", "code_effectif_cepe_cp", CentreLink.CP)),
			Map.entry("/api/effectif-promu-cp",
					new EffectifSqlMeta("effectif_promu_cp", "id_effectif_promu_cp", "code_effectif_promu_cp", CentreLink.CP)),
			Map.entry("/api/effectif-admis-integration-cp",
					new EffectifSqlMeta("effectif_admis_integration_cp", "id_effectif_debut16", "code_effectif_admis_integration_cp",
							CentreLink.CP)),
			Map.entry("/api/effectif-integration-formel-cp",
					new EffectifSqlMeta("effectif_integration_formel_cp", "id_effectif_debut17",
							"code_effectif_integration_formel_cp", CentreLink.CP)),
			Map.entry("/api/effectif-situation-handicap-cp",
					new EffectifSqlMeta("effectif_situation_handicap_cp", "id_effectif_debut19",
							"code_effectif_situation_handicap_cp", CentreLink.CP)),

			Map.entry("/api/effectif-sie",
					new EffectifSqlMeta("effectif_sie", "id_effectif_debut20", "code_effectif_sie", CentreLink.NONE)),
			Map.entry("/api/effectif-abondan-sie",
					new EffectifSqlMeta("effectif_abondan_sie", "id_effectif_debut21", "code_abandon_effectif_sie",
							CentreLink.SIE_OPTIONAL)),
			Map.entry("/api/effectif-promu-sie",
					new EffectifSqlMeta("effectif_promu_sie", "id_effectif_debut22", "code_effectif_promu_sie", CentreLink.NONE)),
			Map.entry("/api/effectif-reverse-formel-sie",
					new EffectifSqlMeta("effectif_reverse_formel_sie", "id_effectif_debut23", "code_effectif_reverse_formel_sie",
							CentreLink.NONE)),
			Map.entry("/api/effectif-situation-handicap-sie",
					new EffectifSqlMeta("effectif_situation_handicap_sie", "id_effectif_debut24",
							"code_effectif_situation_handicap_sie", CentreLink.SIE_OPTIONAL)));

	private WorkflowEffectifSqlSupport() {
	}

	public static EffectifSqlMeta meta(String resourcePath) {
		if (resourcePath == null || resourcePath.isBlank()) {
			return null;
		}
		String path = resourcePath.startsWith("/") ? resourcePath.trim() : "/" + resourcePath.trim();
		return BY_PATH.get(path);
	}

	public static String scopeSql(EffectifSqlMeta meta) {
		return switch (meta.centreLink()) {
			case ALPHA -> """
					SELECT e.%s, a.id_iep, iep.id_drena, ctr.enregistre_par
					FROM %s e
					INNER JOIN alpha a ON e.id_centre = a.id_centre
					INNER JOIN centre ctr ON a.id_centre = ctr.id_centre
					LEFT JOIN iep ON a.id_iep = iep.id_iep
					WHERE e.%s IN :ids
					""".formatted(meta.pkColumn(), meta.table(), meta.pkColumn());
			case CEC -> typedCentreScopeSql(meta, "cec");
			case CP -> typedCentreScopeSql(meta, "cp");
			case SIE -> typedCentreScopeSql(meta, "sie");
			case SIE_OPTIONAL -> """
					SELECT e.%s, c.id_iep, iep.id_drena, ctr.enregistre_par
					FROM %s e
					LEFT JOIN sie c ON e.id_centre = c.id_centre
					LEFT JOIN centre ctr ON c.id_centre = ctr.id_centre
					LEFT JOIN iep ON c.id_iep = iep.id_iep
					WHERE e.%s IN :ids
					""".formatted(meta.pkColumn(), meta.table(), meta.pkColumn());
			case NONE -> """
					SELECT e.%s, NULL, NULL, NULL
					FROM %s e
					WHERE e.%s IN :ids
					""".formatted(meta.pkColumn(), meta.table(), meta.pkColumn());
		};
	}

	private static String typedCentreScopeSql(EffectifSqlMeta meta, String centreType) {
		return """
				SELECT e.%s, c.id_iep, iep.id_drena, ctr.enregistre_par
				FROM %s e
				INNER JOIN %s c ON e.id_centre = c.id_centre
				INNER JOIN centre ctr ON c.id_centre = ctr.id_centre
				LEFT JOIN iep ON c.id_iep = iep.id_iep
				WHERE e.%s IN :ids
				""".formatted(meta.pkColumn(), meta.table(), centreType, meta.pkColumn());
	}

	public static String centreLabelJoin(EffectifSqlMeta meta) {
		return switch (meta.centreLink()) {
			case ALPHA -> """
					INNER JOIN alpha c ON e.id_centre = c.id_centre
					INNER JOIN centre ctr ON c.id_centre = ctr.id_centre""";
			case CEC -> """
					INNER JOIN cec c ON e.id_centre = c.id_centre
					INNER JOIN centre ctr ON c.id_centre = ctr.id_centre""";
			case CP -> """
					INNER JOIN cp c ON e.id_centre = c.id_centre
					INNER JOIN centre ctr ON c.id_centre = ctr.id_centre""";
			case SIE, SIE_OPTIONAL -> """
					LEFT JOIN sie c ON e.id_centre = c.id_centre
					LEFT JOIN centre ctr ON c.id_centre = ctr.id_centre""";
			case NONE -> """
					LEFT JOIN niveau_sie_cec n ON e.id_niveau_sie = n.id_niveau_sie""";
		};
	}

	public static String centreLabelExpr(EffectifSqlMeta meta) {
		return switch (meta.centreLink()) {
			case ALPHA -> """
					TRIM(CONCAT_WS(' ',
						NULLIF(TRIM(ctr.code_centre), ''),
						NULLIF(TRIM(c.libelle_alpha), '')
					))""";
			case CEC, CP, SIE, SIE_OPTIONAL -> "NULLIF(TRIM(ctr.code_centre), '')";
			case NONE -> "NULLIF(TRIM(n.libelle_niveau_sie), '')";
		};
	}
}