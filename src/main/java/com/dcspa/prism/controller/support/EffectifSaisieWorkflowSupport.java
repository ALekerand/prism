package com.dcspa.prism.controller.support;

import com.dcspa.prism.repository.SaisieWorkflowRepository;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.CentreConseillerLabelService;
import com.dcspa.prism.service.SaisieWorkflowService;
import com.dcspa.prism.service.SaisieWorkflowVisibilityRules;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Workflow transversal des saisies effectif (claim, enrichissement liste, purge à la suppression).
 */
@Component
@RequiredArgsConstructor
public class EffectifSaisieWorkflowSupport {

	private static final String WORKFLOW_FEATURE = "SAISIE_DONNEES";

	private final SaisieWorkflowService workflowService;
	private final SaisieWorkflowRepository workflowRepository;
	private final CentreConseillerLabelService auteurLabelService;

	public List<Map<String, Object>> enrichAndFilterList(
			String resourcePath, List<Map<String, Object>> rows, AuthUser user) {
		if (rows.isEmpty()) {
			return rows;
		}
		enrichRows(resourcePath, rows, user);
		if (user == null) {
			return rows;
		}
		return rows.stream().filter(row -> isRowVisible(row, user)).toList();
	}

	public Optional<Map<String, Object>> enrichAndFilterOne(
			String resourcePath, Map<String, Object> row, AuthUser user) {
		enrichRows(resourcePath, List.of(row), user);
		if (user != null && !isRowVisible(row, user)) {
			return Optional.empty();
		}
		return Optional.of(row);
	}

	public void enrichRows(String resourcePath, List<Map<String, Object>> rows, AuthUser user) {
		List<Integer> ids = rows.stream()
				.map(r -> r.get("id"))
				.filter(Integer.class::isInstance)
				.map(Integer.class::cast)
				.toList();
		if (!ids.isEmpty()) {
			Map<Integer, Map<String, Object>> statuses = workflowService.statuses(resourcePath, ids, user);
			for (Map<String, Object> row : rows) {
				Object idObj = row.get("id");
				if (idObj instanceof Integer id && statuses.containsKey(id)) {
					row.putAll(statuses.get(id));
				}
			}
		}
		auteurLabelService.enrichMaps(rows);
	}

	public void claimAfterCreate(String resourcePath, Integer recordId, AuthUser user) {
		claimAfterCreate(resourcePath, recordId, WORKFLOW_FEATURE, user);
	}

	public void claimAfterCreate(String resourcePath, Integer recordId, String workflowFeature, AuthUser user) {
		if (recordId == null) {
			return;
		}
		// Id recyclé après suppression : évite un workflow orphelin (autre propriétaire / SOUMIS).
		purgeOnDelete(resourcePath, recordId);
		if (user == null || !user.hasRole("CONSEILLER")) {
			return;
		}
		workflowService.claim(resourcePath, recordId, workflowFeature, user);
	}

	public void purgeOnDelete(String resourcePath, Integer recordId) {
		if (recordId == null || resourcePath == null || resourcePath.isBlank()) {
			return;
		}
		String normalized = resourcePath.startsWith("/") ? resourcePath : "/" + resourcePath;
		workflowRepository.findByResourcePathAndRecordId(normalized, recordId)
				.ifPresent(workflowRepository::delete);
	}

	private boolean isRowVisible(Map<String, Object> row, AuthUser user) {
		String st = stringOrNull(row.get("workflowStatut"));
		if (st == null) {
			st = "BROUILLON";
		}
		return SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(
				user,
				st,
				stringOrNull(row.get("workflowProprietaire")),
				stringOrNull(row.get("workflowSoumisPar")),
				intOrNull(row.get("idIep")),
				intOrNull(row.get("idDrena")));
	}

	private static String stringOrNull(Object value) {
		if (value == null) {
			return null;
		}
		String s = value.toString().trim();
		return s.isEmpty() ? null : s;
	}

	private static Integer intOrNull(Object value) {
		if (value instanceof Integer i) {
			return i;
		}
		if (value instanceof Number n) {
			return n.intValue();
		}
		if (value instanceof String s && !s.isBlank()) {
			try {
				return Integer.parseInt(s.trim());
			} catch (NumberFormatException ignored) {
				return null;
			}
		}
		return null;
	}
}
