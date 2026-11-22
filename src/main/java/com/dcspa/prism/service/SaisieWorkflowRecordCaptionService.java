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
		WorkflowEffectifSqlSupport.EffectifSqlMeta meta = WorkflowEffectifSqlSupport.meta(path);
		if (meta != null) {
			return captionEffectif(meta, recordIds);
		}
		return Map.of();
	}

	private Map<Integer, String> captionEffectif(
			WorkflowEffectifSqlSupport.EffectifSqlMeta meta, Collection<Integer> recordIds) {
		String joinCentre = WorkflowEffectifSqlSupport.centreLabelJoin(meta);
		String centreLabelExpr = WorkflowEffectifSqlSupport.centreLabelExpr(meta);
		String captionExpr = """
				TRIM(CONCAT_WS(' · ',
					%s,
					NULLIF(TRIM(pa.libelle_periode_activite), ''),
					NULLIF(TRIM(e.%s), '')
				))""".formatted(centreLabelExpr, meta.codeColumn());
		String sql = """
				SELECT e.%s, %s
				FROM %s e
				%s
				LEFT JOIN periode_activite pa ON e.id_periode_activite = pa.id_periode_activite
				WHERE e.%s IN :ids
				""".formatted(meta.pkColumn(), captionExpr, meta.table(), joinCentre, meta.pkColumn());
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

	private static String normalizeResource(String resourcePath) {
		if (resourcePath == null || resourcePath.isBlank()) {
			return "";
		}
		return resourcePath.startsWith("/") ? resourcePath.trim() : "/" + resourcePath.trim();
	}
}