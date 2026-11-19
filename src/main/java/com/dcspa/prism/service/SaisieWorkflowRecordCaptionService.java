package com.dcspa.prism.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/** Libellés lisibles pour les alertes workflow (centre, période, code saisie). */
@Service
public class SaisieWorkflowRecordCaptionService {

	private static final Map<String, String> ALPHA_ACTIVITY_CAPTION_SQL = Map.of(
			"/api/controle",
			"""
					SELECT e.id_controle, %s
					FROM controle e
					INNER JOIN alpha a ON e.id_alpha = a.id_centre
					INNER JOIN centre ctr ON a.id_centre = ctr.id_centre
					LEFT JOIN periode_activite pa ON e.id_periode_activite = pa.id_periode_activite
					WHERE e.id_controle IN :ids
					""",
			"/api/evaluation",
			"""
					SELECT e.id_evaluation, %s
					FROM evaluation e
					INNER JOIN alpha a ON e.id_alpha = a.id_centre
					INNER JOIN centre ctr ON a.id_centre = ctr.id_centre
					LEFT JOIN periode_activite pa ON e.id_periode_activite = pa.id_periode_activite
					WHERE e.id_evaluation IN :ids
					""",
			"/api/visite",
			"""
					SELECT e.id_visite, %s
					FROM visite e
					INNER JOIN alpha a ON e.id_alpha = a.id_centre
					INNER JOIN centre ctr ON a.id_centre = ctr.id_centre
					LEFT JOIN periode_activite pa ON e.id_periode_activite = pa.id_periode_activite
					WHERE e.id_visite IN :ids
					""",
			"/api/performance",
			"""
					SELECT e.id_performance, %s
					FROM performance e
					INNER JOIN alpha a ON e.id_alpha = a.id_centre
					INNER JOIN centre ctr ON a.id_centre = ctr.id_centre
					LEFT JOIN periode_activite pa ON e.id_periode_activite = pa.id_periode_activite
					WHERE e.id_performance IN :ids
					""");

	private static final String ACTIVITY_CAPTION_EXPR = """
			TRIM(CONCAT_WS(' · ',
				NULLIF(TRIM(ctr.code_centre), ''),
				NULLIF(TRIM(a.libelle_alpha), ''),
				NULLIF(TRIM(pa.libelle_periode_activite), '')
			))""";

	@PersistenceContext
	private EntityManager entityManager;

	public Map<Integer, String> resolveCaptions(String resourcePath, Collection<Integer> recordIds) {
		if (recordIds == null || recordIds.isEmpty()) {
			return Map.of();
		}
		String path = normalizeResource(resourcePath);
		if (path.isBlank()) {
			return Map.of();
		}
		String activitySql = ALPHA_ACTIVITY_CAPTION_SQL.get(path);
		if (activitySql != null) {
			return runCaptionQuery(activitySql.formatted(ACTIVITY_CAPTION_EXPR), recordIds);
		}
		if (path.startsWith("/api/effectif")) {
			return captionEffectif(path, recordIds);
		}
		return Map.of();
	}

	private Map<Integer, String> captionEffectif(String resourcePath, Collection<Integer> recordIds) {
		String table = tableFromResource(resourcePath);
		String pk = pkColumnFromResource(resourcePath);
		String codeCol = codeColumnFromResource(resourcePath);
		String joinCentre;
		String centreLabelExpr;
		if (resourcePath.contains("-alpha") || resourcePath.endsWith("alpha")) {
			joinCentre = """
					INNER JOIN alpha c ON e.id_centre = c.id_centre
					INNER JOIN centre ctr ON c.id_centre = ctr.id_centre""";
			centreLabelExpr = """
					TRIM(CONCAT_WS(' ',
						NULLIF(TRIM(ctr.code_centre), ''),
						NULLIF(TRIM(c.libelle_alpha), '')
					))""";
		} else if (resourcePath.contains("cec")) {
			joinCentre = """
					INNER JOIN cec c ON e.id_centre = c.id_centre
					INNER JOIN centre ctr ON c.id_centre = ctr.id_centre""";
			centreLabelExpr = "NULLIF(TRIM(ctr.code_centre), '')";
		} else if (resourcePath.contains("-cp") || resourcePath.contains("cp")) {
			joinCentre = """
					INNER JOIN cp c ON e.id_centre = c.id_centre
					INNER JOIN centre ctr ON c.id_centre = ctr.id_centre""";
			centreLabelExpr = "NULLIF(TRIM(ctr.code_centre), '')";
		} else if (resourcePath.contains("sie")) {
			joinCentre = """
					INNER JOIN sie c ON e.id_centre = c.id_centre
					INNER JOIN centre ctr ON c.id_centre = ctr.id_centre""";
			centreLabelExpr = "NULLIF(TRIM(ctr.code_centre), '')";
		} else {
			return Map.of();
		}
		String captionExpr = """
				TRIM(CONCAT_WS(' · ',
					%s,
					NULLIF(TRIM(pa.libelle_periode_activite), ''),
					NULLIF(TRIM(e.%s), '')
				))""".formatted(centreLabelExpr, codeCol);
		String sql = """
				SELECT e.%s, %s
				FROM %s e
				%s
				LEFT JOIN periode_activite pa ON e.id_periode_activite = pa.id_periode_activite
				WHERE e.%s IN :ids
				""".formatted(pk, captionExpr, table, joinCentre, pk);
		return runCaptionQuery(sql, recordIds);
	}

	@SuppressWarnings("unchecked")
	private Map<Integer, String> runCaptionQuery(String sql, Collection<Integer> recordIds) {
		List<Object[]> rows = entityManager.createNativeQuery(sql)
				.setParameter("ids", recordIds)
				.getResultList();
		Map<Integer, String> out = new HashMap<>();
		for (Object[] row : rows) {
			if (row == null || row.length < 2 || row[0] == null) {
				continue;
			}
			int id = ((Number) row[0]).intValue();
			String caption = row[1] == null ? "" : row[1].toString().trim();
			if (!caption.isEmpty()) {
				out.put(id, caption);
			}
		}
		return out;
	}

	private static String codeColumnFromResource(String resourcePath) {
		return "code_" + tableFromResource(resourcePath);
	}

	private static String tableFromResource(String resourcePath) {
		return resourcePath.substring("/api/".length()).replace('-', '_');
	}

	private static String pkColumnFromResource(String resourcePath) {
		return "id_" + tableFromResource(resourcePath);
	}

	private static String normalizeResource(String resourcePath) {
		if (resourcePath == null || resourcePath.isBlank()) {
			return "";
		}
		return resourcePath.startsWith("/") ? resourcePath.trim() : "/" + resourcePath.trim();
	}
}