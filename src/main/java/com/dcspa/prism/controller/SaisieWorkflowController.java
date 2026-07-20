package com.dcspa.prism.controller;

import com.dcspa.prism.dto.SaisieWorkflowDecisionRequest;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.SaisieWorkflowService;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/saisie-workflows")
@RequiredArgsConstructor
public class SaisieWorkflowController {
	private final SaisieWorkflowService service;

	@Transactional(readOnly = true)
	@GetMapping("/statuses")
	public ResponseEntity<Map<Integer, Map<String, Object>>> statuses(
			@RequestParam String resource,
			@RequestParam String ids,
			@AuthenticationPrincipal AuthUser user) {
		List<Integer> recordIds = Arrays.stream(ids.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.map(Integer::valueOf)
				.toList();
		return ResponseEntity.ok(service.statuses(resource, recordIds, user));
	}

	/** Timeline du cycle de validation courant (snapshot {@code saisie_workflow}). */
	@Transactional(readOnly = true)
	@GetMapping("/historique")
	public ResponseEntity<Map<String, Object>> historique(
			@RequestParam String resource,
			@RequestParam Integer recordId,
			@AuthenticationPrincipal AuthUser user) {
		return ResponseEntity.ok(service.historique(resource, recordId, user));
	}

	@Transactional
	@PostMapping("/claim")
	public ResponseEntity<?> claim(
			@RequestParam String resource,
			@RequestParam Integer recordId,
			@RequestParam(required = false) String feature,
			@AuthenticationPrincipal AuthUser user) {
		try {
			return service.claim(resource, recordId, feature, user);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@PutMapping("/soumettre")
	public ResponseEntity<?> submit(
			@RequestParam String resource,
			@RequestParam Integer recordId,
			@RequestParam(required = false) String feature,
			@AuthenticationPrincipal AuthUser user) {
		try {
			return service.submit(resource, recordId, feature, user);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@PutMapping("/valider")
	public ResponseEntity<?> validate(
			@RequestParam String resource,
			@RequestParam Integer recordId,
			@RequestParam(required = false) String feature,
			@AuthenticationPrincipal AuthUser user) {
		try {
			return service.validate(resource, recordId, feature, user);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@PutMapping("/rejeter")
	public ResponseEntity<?> reject(
			@RequestParam String resource,
			@RequestParam Integer recordId,
			@RequestParam(required = false) String feature,
			@RequestBody(required = false) SaisieWorkflowDecisionRequest request,
			@AuthenticationPrincipal AuthUser user) {
		try {
			return service.reject(resource, recordId, feature, user, request == null ? null : request.getMotif());
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@PutMapping("/retourner")
	public ResponseEntity<?> returnForCorrection(
			@RequestParam String resource,
			@RequestParam Integer recordId,
			@RequestParam(required = false) String feature,
			@RequestBody(required = false) SaisieWorkflowDecisionRequest request,
			@AuthenticationPrincipal AuthUser user) {
		try {
			return service.returnForCorrection(resource, recordId, feature, user, request == null ? null : request.getCommentaire());
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}
}
