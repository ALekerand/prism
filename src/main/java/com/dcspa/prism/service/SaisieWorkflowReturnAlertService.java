package com.dcspa.prism.service;

import com.dcspa.prism.dto.WorkflowRecordScope;
import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.entity.SaisieWorkflow;
import com.dcspa.prism.entity.SaisieWorkflowStatus;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.repository.SaisieWorkflowRepository;
import com.dcspa.prism.security.AuthUser;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaisieWorkflowReturnAlertService {

	private static final int MAX_ITEMS = 12;

	private final SaisieWorkflowRepository workflowRepository;
	private final SaisieWorkflowRecordScopeService recordScopeService;
	private final SaisieWorkflowRecordCaptionService recordCaptionService;
	private final AppUserRepository appUserRepository;

	@Transactional(readOnly = true)
	public Map<String, Object> buildAlertPayload(AuthUser user) {
		Map<String, Object> out = new LinkedHashMap<>();
		if (user == null || !shouldExposeAlerts(user)) {
			out.put("total", 0);
			out.put("items", List.of());
			return out;
		}
		List<Map<String, Object>> items = collectItems(user);
		out.put("total", items.size());
		out.put("items", items);
		return out;
	}

	private boolean shouldExposeAlerts(AuthUser user) {
		if (user == null) {
			return false;
		}
		if (user.hasRole("CONSEILLER") && !user.hasAnyRole("COORDONNATEUR", "IEPP", "SUPERVISEUR", "SUPERVISEUR_AENF",
				"DIRECTEUR", "ADMIN", "SUPER_ADMIN", "SUPER_ROOT")) {
			return true;
		}
		return user.hasAnyRole("COORDONNATEUR", "IEPP", "SUPERVISEUR", "SUPERVISEUR_AENF", "DIRECTEUR");
	}

	private List<Map<String, Object>> collectItems(AuthUser user) {
		List<SaisieWorkflow> returned = workflowRepository.findByStatut(SaisieWorkflowStatus.RETOURNE);
		if (returned.isEmpty()) {
			return List.of();
		}
		Set<String> superiorLogins = resolveSuperiorLogins(returned);
		Map<String, List<SaisieWorkflow>> byResource = returned.stream()
				.collect(Collectors.groupingBy(SaisieWorkflow::getResourcePath));
		Map<String, WorkflowRecordScope> scopeByKey = new HashMap<>();
		Map<String, String> captionByKey = new HashMap<>();
		for (Map.Entry<String, List<SaisieWorkflow>> entry : byResource.entrySet()) {
			List<Integer> ids = entry.getValue().stream().map(SaisieWorkflow::getRecordId).toList();
			Map<Integer, WorkflowRecordScope> scopes = recordScopeService.resolveScopes(entry.getKey(), ids);
			for (Map.Entry<Integer, WorkflowRecordScope> scopeEntry : scopes.entrySet()) {
				scopeByKey.put(scopeKey(entry.getKey(), scopeEntry.getKey()), scopeEntry.getValue());
			}
			Map<Integer, String> captions = recordCaptionService.resolveCaptions(entry.getKey(), ids);
			for (Map.Entry<Integer, String> cap : captions.entrySet()) {
				captionByKey.put(scopeKey(entry.getKey(), cap.getKey()), cap.getValue());
			}
		}

		List<Map<String, Object>> items = new ArrayList<>();
		for (SaisieWorkflow workflow : returned) {
			if (!matchesNotificationRules(workflow, user, superiorLogins)) {
				continue;
			}
			WorkflowRecordScope scope = scopeByKey.get(scopeKey(workflow.getResourcePath(), workflow.getRecordId()));
			if (!visibleInScope(user, workflow, scope)) {
				continue;
			}
			String capKey = scopeKey(workflow.getResourcePath(), workflow.getRecordId());
			items.add(toItem(workflow, captionByKey.get(capKey)));
		}
		items.sort(Comparator.comparing(
				(Map<String, Object> row) -> (Instant) row.get("dateDecision"),
				Comparator.nullsLast(Comparator.reverseOrder())));
		if (items.size() > MAX_ITEMS) {
			return items.subList(0, MAX_ITEMS);
		}
		return items;
	}

	private boolean matchesNotificationRules(SaisieWorkflow workflow, AuthUser user, Set<String> superiorLogins) {
		if (user.hasRole("CONSEILLER") && !user.hasAnyRole("COORDONNATEUR", "IEPP", "SUPERVISEUR", "SUPERVISEUR_AENF",
				"DIRECTEUR", "ADMIN", "SUPER_ADMIN", "SUPER_ROOT")) {
			return SaisieWorkflowReturnNotificationRules.shouldNotify(user, workflow);
		}
		if (user.hasRole("COORDONNATEUR") || user.hasRole("IEPP")) {
			String decidePar = normalize(workflow.getDecidePar());
			boolean superior = decidePar != null && superiorLogins.contains(decidePar);
			return SaisieWorkflowReturnNotificationRules.notifyCoordonnateur(workflow, user)
					&& superior;
		}
		if (user.hasAnyRole("SUPERVISEUR", "SUPERVISEUR_AENF", "DIRECTEUR")) {
			return SaisieWorkflowReturnNotificationRules.shouldNotify(user, workflow);
		}
		return false;
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

	private Set<String> resolveSuperiorLogins(List<SaisieWorkflow> workflows) {
		Set<String> logins = workflows.stream()
				.map(SaisieWorkflow::getDecidePar)
				.map(SaisieWorkflowReturnAlertService::normalize)
				.filter(login -> login != null && !login.isBlank())
				.collect(Collectors.toSet());
		if (logins.isEmpty()) {
			return Set.of();
		}
		Set<String> out = new HashSet<>();
		for (AppUser appUser : appUserRepository.findByUsernameIn(logins)) {
			if (appUser.getUsername() == null) {
				continue;
			}
			boolean superior = appUser.getRoles().stream()
					.map(AppRole::getCodeRole)
					.anyMatch(SaisieWorkflowReturnNotificationRules::isSuperiorRoleCode);
			if (superior) {
				out.add(appUser.getUsername().trim());
			}
		}
		return out;
	}

	private Map<String, Object> toItem(SaisieWorkflow workflow, String recordCaption) {
		Map<String, Object> item = new LinkedHashMap<>();
		item.put("resourcePath", workflow.getResourcePath());
		item.put("resourceLabel", resourceLabel(workflow.getResourcePath()));
		item.put("recordCaption", recordCaption);
		item.put("recordId", workflow.getRecordId());
		item.put("commentaire", workflow.getCommentaireRetour());
		item.put("decidePar", workflow.getDecidePar());
		item.put("dateDecision", workflow.getDateDecision());
		item.put("proprietaire", workflow.getProprietaire());
		return item;
	}

	static String resourceLabel(String resourcePath) {
		if (resourcePath == null || resourcePath.isBlank()) {
			return "Saisie";
		}
		String path = resourcePath.trim();
		return switch (path) {
			case "/api/controle" -> "Contrôle de centre";
			case "/api/evaluation" -> "Évaluation périodique";
			case "/api/visite" -> "Visite conseiller";
			case "/api/performance" -> "Performance";
			case "/api/effectif-alpha" -> "Effectif scolarisé (Alpha)";
			case "/api/effectif-abandon-alpha" -> "Effectif abandon (Alpha)";
			case "/api/effectif-passage-alpha" -> "Effectif passage (Alpha)";
			case "/api/effectif-situation-handicap-alpha" -> "Effectif handicap (Alpha)";
			case "/api/effectif-cp" -> "Effectif scolarisé (CP)";
			case "/api/effectif-abandon-cp" -> "Effectif abandon (CP)";
			case "/api/effectif-cepe-cp" -> "Effectif CEPE (CP)";
			case "/api/effectif-promu-cp" -> "Effectif promu (CP)";
			case "/api/effectif-admis-integration-cp" -> "Effectif admis intégration (CP)";
			case "/api/effectif-integration-formel-cp" -> "Effectif intégration formel (CP)";
			case "/api/effectif-situation-handicap-cp" -> "Effectif handicap (CP)";
			case "/api/effectif-cec" -> "Effectif scolarisé (CEC)";
			case "/api/effectif-abandon-cec" -> "Effectif abandon (CEC)";
			case "/api/effectif-cepe-cec" -> "Effectif CEPE (CEC)";
			case "/api/effectif-promu-cec" -> "Effectif promu (CEC)";
			case "/api/effectif-situation-handicap-cec" -> "Effectif handicap (CEC)";
			case "/api/effectif-sie" -> "Effectif scolarisé (SIE)";
			case "/api/effectif-abondan-sie" -> "Effectif abandon (SIE)";
			case "/api/effectif-promu-sie" -> "Effectif promu (SIE)";
			case "/api/effectif-reverse-formel-sie" -> "Effectif reverse formel (SIE)";
			case "/api/effectif-situation-handicap-sie" -> "Effectif handicap (SIE)";
			default -> path.startsWith("/api/effectif") ? "Effectif" : "Saisie";
		};
	}

	private static String scopeKey(String resourcePath, Integer recordId) {
		return resourcePath + "#" + recordId;
	}

	private static String normalize(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}
}
