package com.dcspa.prism.service;

import com.dcspa.prism.dto.WorkflowRecordScope;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SaisieWorkflowRecordScopeService {

	private static final Map<String, AlphaActivityTable> ALPHA_ACTIVITY_RESOURCES = Map.of(
			"/api/controle", new AlphaActivityTable("controle", "id_controle"),
			"/api/evaluation", new AlphaActivityTable("evaluation", "id_evaluation"),
			"/api/visite", new AlphaActivityTable("visite", "id_visite"),
			"/api/performance", new AlphaActivityTable("performance", "id_performance"));

	@PersistenceContext
	private EntityManager entityManager;

	public Map<Integer, WorkflowRecordScope> resolveScopes(String resourcePath, Collection<Integer> recordIds) {
		if (recordIds == null || recordIds.isEmpty()) {
			return Map.of();
		}
		String path = normalizeResource(resourcePath);
		WorkflowEffectifSqlSupport.EffectifSqlMeta effectif = WorkflowEffectifSqlSupport.meta(path);
		if (effectif != null) {
			return loadEffectifScopes(effectif, recordIds);
		}
		AlphaActivityTable activity = ALPHA_ACTIVITY_RESOURCES.get(path);
		if (activity != null) {
			return loadAlphaActivityScopes(activity, recordIds);
		}
		return Map.of();
	}

	private Map<Integer, WorkflowRecordScope> loadEffectifScopes(
			WorkflowEffectifSqlSupport.EffectifSqlMeta meta, Collection<Integer> recordIds) {
		String sql = WorkflowEffectifSqlSupport.scopeSql(meta);
		return runScopeQuery(sql, recordIds);
	}

	private Map<Integer, WorkflowRecordScope> loadAlphaActivityScopes(
			AlphaActivityTable activity, Collection<Integer> recordIds) {
		String sql = """
				SELECT e.%s, a.id_iep, iep.id_drena, ctr.enregistre_par
				FROM %s e
				INNER JOIN alpha a ON e.id_alpha = a.id_centre
				INNER JOIN centre ctr ON a.id_centre = ctr.id_centre
				LEFT JOIN iep ON a.id_iep = iep.id_iep
				WHERE e.%s IN :ids
				""".formatted(activity.pkColumn(), activity.tableName(), activity.pkColumn());
		return runScopeQuery(sql, recordIds);
	}

	@SuppressWarnings("unchecked")
	private Map<Integer, WorkflowRecordScope> runScopeQuery(String sql, Collection<Integer> recordIds) {
		List<Object[]> rows = entityManager.createNativeQuery(sql)
				.setParameter("ids", recordIds)
				.getResultList();
		Map<Integer, WorkflowRecordScope> out = new HashMap<>();
		for (Object[] row : rows) {
			if (row == null || row.length < 4 || row[0] == null) {
				continue;
			}
			int recordId = ((Number) row[0]).intValue();
			Integer idIep = row[1] instanceof Number n ? n.intValue() : null;
			Integer idDrena = row[2] instanceof Number n ? n.intValue() : null;
			String enregistrePar = row[3] == null ? null : row[3].toString();
			out.put(recordId, new WorkflowRecordScope(idIep, idDrena, enregistrePar));
		}
		return out;
	}

	private static String normalizeResource(String resourcePath) {
		if (resourcePath == null || resourcePath.isBlank()) {
			return "";
		}
		return resourcePath.startsWith("/") ? resourcePath.trim() : "/" + resourcePath.trim();
	}

	private record AlphaActivityTable(String tableName, String pkColumn) {
	}
}