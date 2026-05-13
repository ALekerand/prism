package com.dcspa.prism.controller.support;

import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.SaisieWorkflowService;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Exige que le circuit transversal {@code saisie_workflow} soit au bon statut avant
 * d’appliquer les drapeaux {@link ActivitesCentreWorkflow} sur l’entité (même principe que {@code VisiteController}).
 */
@Service
@RequiredArgsConstructor
public class ActivitesCentreSaisieWorkflowGate {
	private final SaisieWorkflowService saisieWorkflowService;

	/** Réponse d’erreur à renvoyer au client, ou vide si la validation transversale a réussi. */
	public Optional<ResponseEntity<?>> transversalValidate(
			String resourcePath, int recordId, String permissionFeature, AuthUser user) {
		ResponseEntity<?> workflowResponse;
		try {
			workflowResponse = saisieWorkflowService.validate(resourcePath, recordId, permissionFeature, user);
		} catch (IllegalArgumentException ex) {
			return Optional.of(ResponseEntity.badRequest().body(Map.of("message", ex.getMessage())));
		}
		if (!workflowResponse.getStatusCode().is2xxSuccessful()) {
			return Optional.of(workflowResponse);
		}
		return Optional.empty();
	}
}
