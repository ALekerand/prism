package com.dcspa.prism.service;

import com.dcspa.prism.dto.WorkflowRecordScope;
import com.dcspa.prism.entity.SaisieWorkflow;
import com.dcspa.prism.entity.SaisieWorkflowStatus;
import com.dcspa.prism.repository.SaisieWorkflowRepository;
import com.dcspa.prism.security.AuthUser;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Alertes tableau de bord : saisies {@link SaisieWorkflowListTab#ACTION} pour les validateurs. */
@Service
@RequiredArgsConstructor
public class SaisieWorkflowPendingAlertService {

	private static final int MAX_ITEMS = 12;

	private static final List<SaisieWorkflowStatus> PENDING_STATUSES = List.of(
			SaisieWorkflowStatus.SOUMIS,
			SaisieWorkflowStatus.VALIDEE_COORDONNATEUR,
			SaisieWorkflowStatus.VALIDEE_SUPERVISEUR);

	private final SaisieWorkflowRepository workflowRepository;
	private final SaisieWorkflowRecordScopeService recordScopeService;
	private final SaisieWorkflowRecordCaptionService recordCaptionService;

	@Transactional(readOnly = true)
	public Map<String, Object> buildAlertPayload(AuthUser user) {
		Map<String, Object> out = new LinkedHashMap<>();
		if (!shouldExpose(user)) {
			out.put("total", 0);
			out.put("items", List.of());
			return out;
		}
		List<Map<String, Object>> items = collectItems(user);
		out.put("total", items.size());
		out.put("items", items);
		return out;
	}

	private boolean shouldExpose(AuthUser user) {
		if (user == null) {
			return false;
		}
		return user.hasAnyRole(
				"COORDONNATEUR",
				"IEPP",
				"SUPERVISEUR",
				"SUPERVISEUR_AENF",
				"DIRECTEUR");
	}

	private List<Map<String, Object>> collectItems(AuthUser user) {
		Set<String> seen = new LinkedHashSet<>();
		List<SaisieWorkflow> candidates = new ArrayList<>();
		for (SaisieWorkflowStatus status : PENDING_STATUSES) {
			for (SaisieWorkflow workflow : workflowRepository.findByStatut(status)) {
				String key = workflow.getResourcePath() + "#" + workflow.getRecordId();
				if (seen.add(key)) {
					candidates.add(workflow);
				}
			}
		}
		if (candidates.isEmpty()) {
			return List.of();
		}
		Map<String, WorkflowRecordScope> scopeByKey = new HashMap<>();
		Map<String, String> captionByKey = new HashMap<>();
		resolveScopesAndCaptions(candidates, scopeByKey, captionByKey);

		List<Map<String, Object>> items = new ArrayList<>();
		for (SaisieWorkflow workflow : candidates) {
			if (SaisieWorkflowQueueTabResolver.resolve(workflow, user) != SaisieWorkflowListTab.ACTION) {
				continue;
			}
			WorkflowRecordScope scope = scopeByKey.get(scopeKey(workflow.getResourcePath(), workflow.getRecordId()));
			if (!visibleInScope(user, workflow, scope)) {
				continue;
			}
			String cap = captionByKey.get(scopeKey(workflow.getResourcePath(), workflow.getRecordId()));
			items.add(toItem(workflow, cap));
		}
		items.sort(Comparator.comparing(
				(Map<String, Object> row) -> (Instant) row.get("dateSoumission"),
				Comparator.nullsLast(Comparator.naturalOrder())));
		if (items.size() > MAX_ITEMS) {
			return items.subList(0, MAX_ITEMS);
		}
		return items;
	}

	private void resolveScopesAndCaptions(
			List<SaisieWorkflow> workflows,
			Map<String, WorkflowRecordScope> scopeByKey,
			Map<String, String> captionByKey) {
		Map<String, List<Integer>> idsByResource = new HashMap<>();
		for (SaisieWorkflow workflow : workflows) {
			idsByResource
					.computeIfAbsent(workflow.getResourcePath(), key -> new ArrayList<>())
					.add(workflow.getRecordId());
		}
		for (Map.Entry<String, List<Integer>> entry : idsByResource.entrySet()) {
			Map<Integer, WorkflowRecordScope> scopes = recordScopeService.resolveScopes(entry.getKey(), entry.getValue());
			for (Map.Entry<Integer, WorkflowRecordScope> scopeEntry : scopes.entrySet()) {
				scopeByKey.put(scopeKey(entry.getKey(), scopeEntry.getKey()), scopeEntry.getValue());
			}
			Map<Integer, String> captions = recordCaptionService.resolveCaptions(entry.getKey(), entry.getValue());
			for (Map.Entry<Integer, String> cap : captions.entrySet()) {
				captionByKey.put(scopeKey(entry.getKey(), cap.getKey()), cap.getValue());
			}
		}
	}

	private boolean visibleInScope(AuthUser user, SaisieWorkflow workflow, WorkflowRecordScope scope) {
		Integer idIep = scope == null ? null : scope.idIep();
		Integer idDrena = scope == null ? null : scope.idDrena();
		return SaisieWorkflowVisibilityRules.rowVisibleInCommissionList(
				user,
				workflow.getStatut().name(),
				workflow.getProprietaire(),
				workflow.getSoumisPar(),
				idIep,
				idDrena);
	}

	private Map<String, Object> toItem(SaisieWorkflow workflow, String recordCaption) {
		Map<String, Object> item = new LinkedHashMap<>();
		item.put("resourcePath", workflow.getResourcePath());
		item.put("resourceLabel", SaisieWorkflowReturnAlertService.resourceLabel(workflow.getResourcePath()));
		item.put("recordCaption", recordCaption);
		item.put("recordId", workflow.getRecordId());
		item.put("proprietaire", workflow.getProprietaire());
		item.put("soumisPar", workflow.getSoumisPar());
		item.put("dateSoumission", workflow.getDateSoumission());
		return item;
	}

	private static String scopeKey(String resourcePath, Integer recordId) {
		return resourcePath + "#" + recordId;
	}
}