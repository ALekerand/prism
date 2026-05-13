package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.VisiteRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.Visite;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.VisiteRepository;
import com.dcspa.prism.service.SaisieWorkflowService;
import com.dcspa.prism.service.VisiteWorkflowListRules;
import com.dcspa.prism.security.AuthUser;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visite")
@RequiredArgsConstructor
public class VisiteController {
	private static final String VISITE_WORKFLOW_RESOURCE = "/api/visite";
	private static final String VISITE_VALIDATION_FEATURE = "VALIDATION_VISITES_CONSEILLER";

	private final VisiteRepository repository;
	private final AlphaRepository alphaRepository;
	private final SaisieWorkflowService saisieWorkflowService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll(@AuthenticationPrincipal AuthUser user) {
		List<Visite> all = repository.findAll();
		Map<Integer, Map<String, Object>> workflowById = workflowStatusesFor(all, user);
		return ResponseEntity.ok(all.stream()
				.filter(v -> isVisiteVisibleForUser(v, workflowById, user))
				.map(this::toRow)
				.toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		return repository.findById(id)
				.filter(v -> isVisiteVisibleForUser(v, workflowStatusesFor(List.of(v), user), user))
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		Optional<Visite> opt = repository.findById(id);
		if (opt.isEmpty() || !isVisiteVisibleForUser(opt.get(), workflowStatusesFor(List.of(opt.get()), user), user)) {
			return ResponseEntity.notFound().build();
		}
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Transactional
	@PutMapping("/{id}/valider-coordonnateur")
	public ResponseEntity<?> validateByCoordonnateur(
			@PathVariable Integer id,
			@AuthenticationPrincipal AuthUser user) {
		Optional<Visite> opt = repository.findById(id);
		if (opt.isEmpty()
				|| !isVisiteVisibleForUser(opt.get(), workflowStatusesFor(List.of(opt.get()), user), user)) {
			return ResponseEntity.notFound().build();
		}
		ResponseEntity<?> workflowResponse;
		try {
			workflowResponse = saisieWorkflowService.validate(
					VISITE_WORKFLOW_RESOURCE, id, VISITE_VALIDATION_FEATURE, user);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
		if (!workflowResponse.getStatusCode().is2xxSuccessful()) {
			return workflowResponse;
		}
		Visite e = opt.get();
		e.setValideeCoordonnateur(true);
		return ResponseEntity.ok(toRow(repository.save(e)));
	}

	@Transactional(readOnly = true)
	@GetMapping("/search")
	public ResponseEntity<List<Map<String, Object>>> search(@RequestParam(required = false) Integer idAlpha,
			@RequestParam(required = false) String maitriseSeanceLecture,
			@AuthenticationPrincipal AuthUser user) {
		String m = maitriseSeanceLecture == null ? null : maitriseSeanceLecture.toLowerCase();
		List<Visite> filtered = repository.findAll().stream()
				.filter(x -> idAlpha == null || idAlpha.equals(JpaAssociationIds.intIdOrNull(x.getIdAlpha())))
				.filter(x -> m == null || (x.getMaitriseSeanceLecture() != null && x.getMaitriseSeanceLecture().toLowerCase().contains(m)))
				.toList();
		Map<Integer, Map<String, Object>> workflowById = workflowStatusesFor(filtered, user);
		return ResponseEntity.ok(filtered.stream()
				.filter(v -> isVisiteVisibleForUser(v, workflowById, user))
				.map(this::toRow)
				.toList());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<?> create(@RequestBody VisiteRequest body) {
		try {
			validatePointsCreation(body);
			Visite e = new Visite();
			apply(e, body);
			return ResponseEntity.status(201).body(toRow(repository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<?> update(
			@PathVariable Integer id,
			@RequestBody VisiteRequest body,
			@AuthenticationPrincipal AuthUser user) {
		Optional<Visite> opt = repository.findById(id);
		if (opt.isEmpty()
				|| !isVisiteVisibleForUser(opt.get(), workflowStatusesFor(List.of(opt.get()), user), user)) {
			return ResponseEntity.notFound().build();
		}
		try {
			Visite e = opt.get();
			validateWorkflowLock(e, body);
			apply(e, body);
			return ResponseEntity.ok(toRow(repository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	private void apply(Visite e, VisiteRequest r) {
		if (r == null || r.getIdAlpha() == null) throw new IllegalArgumentException("idAlpha est obligatoire");
		Alpha alpha = alphaRepository.findById(r.getIdAlpha()).orElseThrow(() -> new IllegalArgumentException("Alpha introuvable: " + r.getIdAlpha()));
		e.setIdAlpha(alpha);
		e.setMaitriseSeanceLecture(r.getMaitriseSeanceLecture());
		e.setMaitriseSeanceEcriture(r.getMaitriseSeanceEcriture());
		e.setMaitriseSeanceCalcul(r.getMaitriseSeanceCalcul());
		e.setMaitriseSeanceCvc(r.getMaitriseSeanceCvc());
		if (e.getValideeCoordonnateur() == null) {
			e.setValideeCoordonnateur(false);
		}
	}

	private Map<String, Object> toRow(Visite e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "Alpha", e.getIdAlpha());
		int totalVisitesConseiller = Math.toIntExact(repository.countByIdAlpha_Id(JpaAssociationIds.intIdOrNull(e.getIdAlpha())));
		m.put("maitriseSeanceLecture", e.getMaitriseSeanceLecture());
		m.put("maitriseSeanceEcriture", e.getMaitriseSeanceEcriture());
		m.put("maitriseSeanceCalcul", e.getMaitriseSeanceCalcul());
		m.put("maitriseSeanceCvc", e.getMaitriseSeanceCvc());
		m.put("nombreVisiteRealiseParConseiller", totalVisitesConseiller);
		m.put("nombreBulletinEffectueParConseiller", totalVisitesConseiller);
		m.put("nombreVisiteConseillerSuperviseurEffectue", e.getNombreVisiteConseillerSuperviseurEffectue());
		m.put("nombreReunionBilanConseillerSuperviseur", e.getNombreReunionBilanConseillerSuperviseur());
		m.put("nombreVisiteEffectueParIepp", e.getNombreVisiteEffectueParIepp());
		m.put("nombreReunionPointActiviteAlpha", e.getNombreReunionPointActiviteAlpha());
		m.put("valideeCoordonnateur", Boolean.TRUE.equals(e.getValideeCoordonnateur()));
		return m;
	}

	private void validatePointsCreation(VisiteRequest body) {
		if (body == null || body.getIdAlpha() == null) throw new IllegalArgumentException("idAlpha est obligatoire");
		String mode = normalizeMode(body.getMode());
		if (mode != null && !"points".equals(mode)) {
			throw new IllegalArgumentException("Création impossible : utilisez la modification pour le suivi de visite.");
		}
	}

	private void validateWorkflowLock(Visite existing, VisiteRequest body) {
		String mode = normalizeMode(body == null ? null : body.getMode());
		if (("points".equals(mode) || "conseiller".equals(mode)) && hasSupervisorFollowup(existing)) {
			throw new IllegalArgumentException("Modification impossible : le superviseur a déjà effectué son suivi.");
		}
		if ("superviseur".equals(mode) && hasIeppFollowup(existing)) {
			throw new IllegalArgumentException("Modification impossible : l’IEPP a déjà effectué son suivi.");
		}
		if (mode == null && hasSupervisorFollowup(existing) && changesConseillerScope(existing, body)) {
			throw new IllegalArgumentException("Modification impossible : le superviseur a déjà effectué son suivi.");
		}
		if (mode == null && hasIeppFollowup(existing) && changesSuperviseurScope(existing, body)) {
			throw new IllegalArgumentException("Modification impossible : l’IEPP a déjà effectué son suivi.");
		}
	}

	private String normalizeMode(String mode) {
		if (mode == null || mode.isBlank()) return null;
		String normalized = mode.trim().toLowerCase();
		return switch (normalized) {
			case "points", "conseiller", "superviseur", "iepp" -> normalized;
			default -> throw new IllegalArgumentException("Mode de modification visite invalide: " + mode);
		};
	}

	private boolean hasSupervisorFollowup(Visite e) {
		return e.getNombreVisiteConseillerSuperviseurEffectue() != null || e.getNombreReunionBilanConseillerSuperviseur() != null;
	}

	private boolean hasIeppFollowup(Visite e) {
		return e.getNombreVisiteEffectueParIepp() != null || e.getNombreReunionPointActiviteAlpha() != null;
	}

	private boolean changesConseillerScope(Visite existing, VisiteRequest body) {
		if (body == null) return false;
		return changed(existing.getMaitriseSeanceLecture(), body.getMaitriseSeanceLecture())
				|| changed(existing.getMaitriseSeanceEcriture(), body.getMaitriseSeanceEcriture())
				|| changed(existing.getMaitriseSeanceCalcul(), body.getMaitriseSeanceCalcul())
				|| changed(existing.getMaitriseSeanceCvc(), body.getMaitriseSeanceCvc())
				|| changed(existing.getNombreVisiteRealiseParConseiller(), body.getNombreVisiteRealiseParConseiller())
				|| changed(existing.getNombreBulletinEffectueParConseiller(), body.getNombreBulletinEffectueParConseiller());
	}

	private boolean changesSuperviseurScope(Visite existing, VisiteRequest body) {
		if (body == null) return false;
		return changed(existing.getNombreVisiteConseillerSuperviseurEffectue(), body.getNombreVisiteConseillerSuperviseurEffectue())
				|| changed(existing.getNombreReunionBilanConseillerSuperviseur(), body.getNombreReunionBilanConseillerSuperviseur());
	}

	private boolean changed(Object current, Object incoming) {
		return !java.util.Objects.equals(current, incoming);
	}

	private Map<Integer, Map<String, Object>> workflowStatusesFor(List<Visite> visites, AuthUser user) {
		List<Integer> ids = visites.stream().map(Visite::getId).filter(Objects::nonNull).toList();
		return saisieWorkflowService.statuses(VISITE_WORKFLOW_RESOURCE, ids, user);
	}

	private boolean isVisiteVisibleForUser(Visite e, Map<Integer, Map<String, Object>> workflowById, AuthUser user) {
		Integer id = e.getId();
		Map<String, Object> w = id == null ? null : workflowById.get(id);
		return VisiteWorkflowListRules.isRowVisible(e, w, user);
	}
}
