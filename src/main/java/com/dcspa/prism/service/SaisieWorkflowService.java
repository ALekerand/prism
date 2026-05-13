package com.dcspa.prism.service;

import com.dcspa.prism.controller.support.PermissionGuard;
import com.dcspa.prism.entity.SaisieWorkflow;
import com.dcspa.prism.entity.SaisieWorkflowStatus;
import com.dcspa.prism.repository.SaisieWorkflowRepository;
import com.dcspa.prism.security.AuthUser;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaisieWorkflowService {
	private static final String[] ADMIN_ROLES = { "ADMIN", "SUPER_ADMIN", "SUPER_ROOT" };

	private final SaisieWorkflowRepository repository;

	@Transactional(readOnly = true)
	public Map<Integer, Map<String, Object>> statuses(String resourcePath, Collection<Integer> recordIds, AuthUser user) {
		Map<Integer, Map<String, Object>> out = new LinkedHashMap<>();
		for (Integer id : recordIds) {
			if (id != null) {
				out.put(id, draftRow(resourcePath, id));
			}
		}
		List<SaisieWorkflow> workflows = repository.findByResourcePathAndRecordIdIn(normalizeResource(resourcePath), out.keySet());
		for (SaisieWorkflow workflow : workflows) {
			out.put(workflow.getRecordId(), toRow(workflow, user));
		}
		return out;
	}

	@Transactional(readOnly = true)
	public boolean isEditable(String resourcePath, Integer recordId) {
		return isEditable(resourcePath, recordId, null);
	}

	/**
	 * Prise en charge du périmètre « conseiller = ses brouillons uniquement » lorsque {@code user} est renseigné.
	 */
	@Transactional(readOnly = true)
	public boolean isEditable(String resourcePath, Integer recordId, AuthUser user) {
		return repository.findByResourcePathAndRecordId(normalizeResource(resourcePath), recordId)
				.map(w -> isEditable(w, user))
				.orElse(true);
	}

	@Transactional
	public ResponseEntity<?> claim(String resourcePath, Integer recordId, String feature, AuthUser user) {
		ResponseEntity<?> denied = requirePermission(user, feature, "MODIFIER");
		if (denied != null) {
			return denied;
		}
		if (user == null || !user.hasRole("CONSEILLER")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(Map.of("message", "Seul un conseiller peut revendiquer une saisie."));
		}
		String me = username(user);
		SaisieWorkflow workflow = getOrCreate(resourcePath, recordId);
		if (workflow.getId() != null) {
			if (workflow.getProprietaire() != null && !workflow.getProprietaire().equals(me)) {
				return badRequest("Cette saisie est déjà attribuée à un autre utilisateur.");
			}
			if (workflow.getStatut() != SaisieWorkflowStatus.BROUILLON
					&& workflow.getStatut() != SaisieWorkflowStatus.RETOURNE) {
				return badRequest("Impossible de revendiquer une donnée déjà soumise ou validée.");
			}
		}
		workflow.setProprietaire(me);
		return ResponseEntity.ok(toRow(repository.save(workflow), user));
	}

	@Transactional
	public ResponseEntity<?> submit(String resourcePath, Integer recordId, String feature, AuthUser user) {
		ResponseEntity<?> denied = requirePermission(user, feature, "MODIFIER");
		if (denied != null) return denied;
		SaisieWorkflow workflow = getOrCreate(resourcePath, recordId);
		if (!isEditable(workflow, user)) {
			return badRequest("Soumission impossible : la donnée est déjà engagée dans le circuit de validation.");
		}
		if (workflow.getProprietaire() == null && username(user) != null) {
			workflow.setProprietaire(username(user));
		}
		workflow.setStatut(SaisieWorkflowStatus.SOUMIS);
		workflow.setMotifRejet(null);
		workflow.setCommentaireRetour(null);
		workflow.setSoumisPar(username(user));
		workflow.setDateSoumission(Instant.now());
		workflow.setDecidePar(null);
		workflow.setDateDecision(null);
		return ResponseEntity.ok(toRow(repository.save(workflow), user));
	}

	@Transactional
	public ResponseEntity<?> validate(String resourcePath, Integer recordId, String feature, AuthUser user) {
		SaisieWorkflow workflow = getExisting(resourcePath, recordId);
		RoleDecision decision = nextDecision(workflow);
		if (decision == null || decision.nextStatus == null) {
			return badRequest("Validation impossible pour le statut courant.");
		}
		ResponseEntity<?> denied = requireValidator(user, feature, decision.roles);
		if (denied != null) return denied;
		workflow.setStatut(decision.nextStatus);
		workflow.setMotifRejet(null);
		workflow.setCommentaireRetour(null);
		workflow.setDecidePar(username(user));
		workflow.setDateDecision(Instant.now());
		return ResponseEntity.ok(toRow(repository.save(workflow)));
	}

	@Transactional
	public ResponseEntity<?> reject(String resourcePath, Integer recordId, String feature, AuthUser user, String motif) {
		String cleanMotif = clean(motif);
		if (cleanMotif == null) {
			return badRequest("Le motif de rejet est obligatoire.");
		}
		SaisieWorkflow workflow = getExisting(resourcePath, recordId);
		ResponseEntity<?> denied = requireCurrentValidator(workflow, feature, user);
		if (denied != null) return denied;
		workflow.setStatut(SaisieWorkflowStatus.REJETE);
		workflow.setMotifRejet(cleanMotif);
		workflow.setCommentaireRetour(null);
		workflow.setDecidePar(username(user));
		workflow.setDateDecision(Instant.now());
		return ResponseEntity.ok(toRow(repository.save(workflow)));
	}

	@Transactional
	public ResponseEntity<?> returnForCorrection(String resourcePath, Integer recordId, String feature, AuthUser user, String commentaire) {
		SaisieWorkflow workflow = getExisting(resourcePath, recordId);
		ResponseEntity<?> denied = requireCurrentValidator(workflow, feature, user);
		if (denied != null) return denied;
		workflow.setStatut(SaisieWorkflowStatus.RETOURNE);
		workflow.setMotifRejet(null);
		workflow.setCommentaireRetour(clean(commentaire));
		workflow.setDecidePar(username(user));
		workflow.setDateDecision(Instant.now());
		return ResponseEntity.ok(toRow(repository.save(workflow)));
	}

	public Map<String, Object> toRow(SaisieWorkflow workflow) {
		return toRow(workflow, null);
	}

	public Map<String, Object> toRow(SaisieWorkflow workflow, AuthUser user) {
		Map<String, Object> row = draftRow(workflow.getResourcePath(), workflow.getRecordId());
		row.put("workflowStatut", workflow.getStatut().name());
		row.put("workflowStatutLibelle", label(workflow.getStatut()));
		row.put("workflowEditable", isEditable(workflow, user));
		row.put("workflowMotifRejet", workflow.getMotifRejet());
		row.put("workflowCommentaireRetour", workflow.getCommentaireRetour());
		row.put("workflowSoumisPar", workflow.getSoumisPar());
		row.put("workflowProprietaire", workflow.getProprietaire());
		row.put("workflowDecidePar", workflow.getDecidePar());
		row.put("workflowDateSoumission", workflow.getDateSoumission());
		row.put("workflowDateDecision", workflow.getDateDecision());
		return row;
	}

	private SaisieWorkflow getOrCreate(String resourcePath, Integer recordId) {
		String resource = normalizeResource(resourcePath);
		return repository.findByResourcePathAndRecordId(resource, recordId).orElseGet(() -> {
			SaisieWorkflow workflow = new SaisieWorkflow();
			workflow.setResourcePath(resource);
			workflow.setRecordId(recordId);
			workflow.setStatut(SaisieWorkflowStatus.BROUILLON);
			return workflow;
		});
	}

	private SaisieWorkflow getExisting(String resourcePath, Integer recordId) {
		return repository.findByResourcePathAndRecordId(normalizeResource(resourcePath), recordId)
				.orElseThrow(() -> new IllegalArgumentException("Cette donnée n'a pas encore été soumise."));
	}

	private Map<String, Object> draftRow(String resourcePath, Integer recordId) {
		Map<String, Object> row = new LinkedHashMap<>();
		row.put("resourcePath", normalizeResource(resourcePath));
		row.put("recordId", recordId);
		row.put("workflowStatut", SaisieWorkflowStatus.BROUILLON.name());
		row.put("workflowStatutLibelle", label(SaisieWorkflowStatus.BROUILLON));
		row.put("workflowEditable", true);
		row.put("workflowMotifRejet", null);
		row.put("workflowCommentaireRetour", null);
		row.put("workflowProprietaire", null);
		row.put("workflowSoumisPar", null);
		return row;
	}

	/**
	 * Édition / suppression autorisées seulement en {@link SaisieWorkflowStatus#BROUILLON} ou
	 * {@link SaisieWorkflowStatus#RETOURNE} (retour validateur « retourner pour correction »).
	 * Après soumission ({@link SaisieWorkflowStatus#SOUMIS} et suivants jusqu’à validation centrale ou rejet),
	 * le saisisseur ne peut plus modifier ni supprimer l’enregistrement via l’API (filtre HTTP + UI).
	 */
	private boolean isEditable(SaisieWorkflow workflow, AuthUser user) {
		if (!(workflow.getStatut() == SaisieWorkflowStatus.BROUILLON
				|| workflow.getStatut() == SaisieWorkflowStatus.RETOURNE)) {
			return false;
		}
		if (user == null || user.hasAnyRole(ADMIN_ROLES)) {
			return true;
		}
		if (!user.hasRole("CONSEILLER")) {
			return true;
		}
		String me = username(user);
		if (workflow.getProprietaire() == null) {
			return true;
		}
		return Objects.equals(me, workflow.getProprietaire());
	}

	private RoleDecision nextDecision(SaisieWorkflow workflow) {
		return switch (workflow.getStatut()) {
			case SOUMIS -> new RoleDecision(SaisieWorkflowStatus.VALIDEE_COORDONNATEUR, new String[] { "COORDONNATEUR" });
			case VALIDEE_COORDONNATEUR -> new RoleDecision(SaisieWorkflowStatus.VALIDEE_SUPERVISEUR, new String[] { "SUPERVISEUR" });
			case VALIDEE_SUPERVISEUR -> new RoleDecision(SaisieWorkflowStatus.VALIDEE_CENTRALE, new String[] { "SUPERVISEUR_AENF", "DIRECTEUR" });
			case BROUILLON, VALIDEE_CENTRALE, REJETE, RETOURNE -> null;
		};
	}

	private ResponseEntity<?> requireCurrentValidator(SaisieWorkflow workflow, String feature, AuthUser user) {
		RoleDecision decision = nextDecision(workflow);
		if (decision == null) {
			return badRequest("Décision impossible pour le statut courant.");
		}
		return requireValidator(user, feature, decision.roles);
	}

	private ResponseEntity<?> requireValidator(AuthUser user, String feature, String... roles) {
		ResponseEntity<?> denied = requirePermission(user, feature, "VALIDER");
		if (denied != null) return denied;
		if (user != null && (user.hasAnyRole(roles) || user.hasAnyRole(ADMIN_ROLES))) {
			return null;
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(Map.of("message", "Accès refusé: rôle " + Arrays.toString(roles) + " requis"));
	}

	private ResponseEntity<?> requirePermission(AuthUser user, String feature, String permission) {
		String cleanFeature = clean(feature);
		if (cleanFeature == null) {
			return null;
		}
		return PermissionGuard.require(user, cleanFeature, permission);
	}

	private String label(SaisieWorkflowStatus status) {
		return switch (status) {
			case BROUILLON -> "Brouillon";
			case SOUMIS -> "Soumis pour validation";
			case VALIDEE_COORDONNATEUR -> "Validé coordonnateur";
			case VALIDEE_SUPERVISEUR -> "Validé superviseur";
			case VALIDEE_CENTRALE -> "Validé central";
			case REJETE -> "Rejeté";
			case RETOURNE -> "Retourné pour correction";
		};
	}

	private static String normalizeResource(String resourcePath) {
		String clean = clean(resourcePath);
		if (clean == null) {
			throw new IllegalArgumentException("Ressource obligatoire");
		}
		return clean.startsWith("/") ? clean : "/" + clean;
	}

	private static String clean(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}

	private static String username(AuthUser user) {
		return user == null ? null : user.getUsername();
	}

	private record RoleDecision(SaisieWorkflowStatus nextStatus, String[] roles) {
	}

	private ResponseEntity<?> badRequest(String message) {
		return ResponseEntity.badRequest().body(Map.of("message", message));
	}
}
