package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ActivitesCentreSaisieWorkflowGate;
import com.dcspa.prism.controller.support.ActivitesCentreWorkflow;
import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.PermissionGuard;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.EvaluationRequest;
import com.dcspa.prism.entity.Evaluation;
import com.dcspa.prism.entity.EvaluationThemeTaux;
import com.dcspa.prism.entity.NiveauEvaluation;
import com.dcspa.prism.entity.PeriodeActivite;
import com.dcspa.prism.entity.TauxEvaluation;
import com.dcspa.prism.entity.ThemeEvaluation;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.EvaluationRepository;
import com.dcspa.prism.repository.NiveauEvaluationRepository;
import com.dcspa.prism.repository.PeriodeActiviteRepository;
import com.dcspa.prism.repository.TauxEvaluationRepository;
import com.dcspa.prism.repository.ThemeEvaluationRepository;
import com.dcspa.prism.security.AuthUser;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluation")
@RequiredArgsConstructor
public class EvaluationController {
	private static final String FEATURE = "ACTIVITES_CENTRE_EVALUATION";
	private static final String WORKFLOW_RESOURCE = "/api/evaluation";
	private final EvaluationRepository repository;
	private final ActivitesCentreSaisieWorkflowGate saisieWorkflowGate;
	private final AlphaRepository alphaRepository;
	private final PeriodeActiviteRepository periodeRepository;
	private final NiveauEvaluationRepository niveauRepository;
	private final ThemeEvaluationRepository themeRepository;
	private final TauxEvaluationRepository tauxRepository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<?> findAll(@AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "LIRE");
		if (denied != null) return denied;
		return ResponseEntity.ok(repository.findAllWithRefs().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "LIRE");
		if (denied != null) return denied;
		return repository.findByIdWithRefs(id).map(this::toRow).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "MODIFIER");
		if (denied != null) return denied;
		Optional<Evaluation> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		try {
			ActivitesCentreWorkflow.ensureEditable(opt.get());
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Transactional(readOnly = true)
	@GetMapping("/search")
	public ResponseEntity<?> search(
			@RequestParam(required = false) Integer idAlpha,
			@RequestParam(required = false) Integer idPeriodeActivite,
			@RequestParam(required = false) Integer idNiveauEvaluation,
			@RequestParam(required = false) Integer idThemeEvaluation,
			@RequestParam(required = false) Integer idTauxEvaluation,
			@AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "LIRE");
		if (denied != null) return denied;
		return ResponseEntity.ok(repository.findAllWithRefs().stream()
				.filter(x -> idAlpha == null || idAlpha.equals(JpaAssociationIds.intIdOrNull(x.getIdAlpha())))
				.filter(x -> idPeriodeActivite == null || idPeriodeActivite.equals(JpaAssociationIds.intIdOrNull(x.getIdPeriodeActivite())))
				.filter(x -> idNiveauEvaluation == null || idNiveauEvaluation.equals(JpaAssociationIds.intIdOrNull(x.getIdNiveauEvaluation())))
				.filter(x -> idThemeEvaluation == null || idThemeEvaluation.equals(JpaAssociationIds.intIdOrNull(x.getIdThemeEvaluation())))
				.filter(x -> idTauxEvaluation == null || idTauxEvaluation.equals(JpaAssociationIds.intIdOrNull(x.getIdTauxEvaluation())))
				.map(this::toRow).toList());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<?> create(@RequestBody EvaluationRequest body, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "CREER");
		if (denied != null) return denied;
		try {
			Evaluation e = new Evaluation();
			apply(e, body);
			ActivitesCentreWorkflow.initializeDraft(e);
			return ResponseEntity.status(201).body(toRow(repository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody EvaluationRequest body, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "MODIFIER");
		if (denied != null) return denied;
		Optional<Evaluation> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		try {
			Evaluation e = opt.get();
			ActivitesCentreWorkflow.ensureEditable(e);
			apply(e, body);
			return ResponseEntity.ok(toRow(repository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@Transactional
	@PutMapping("/{id}/valider-coordonnateur")
	public ResponseEntity<?> validateCoordonnateur(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		Optional<Evaluation> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		Optional<ResponseEntity<?>> workflowDenied =
				saisieWorkflowGate.transversalValidate(WORKFLOW_RESOURCE, id, FEATURE, user);
		if (workflowDenied.isPresent()) return workflowDenied.get();
		Evaluation e = opt.get();
		ResponseEntity<?> denied = ActivitesCentreWorkflow.validateCoordonnateur(e, user, FEATURE);
		if (denied != null) return denied;
		return ResponseEntity.ok(toRow(repository.save(e)));
	}

	@Transactional
	@PutMapping("/{id}/valider-superviseur")
	public ResponseEntity<?> validateSuperviseur(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		Optional<Evaluation> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		Optional<ResponseEntity<?>> workflowDenied =
				saisieWorkflowGate.transversalValidate(WORKFLOW_RESOURCE, id, FEATURE, user);
		if (workflowDenied.isPresent()) return workflowDenied.get();
		Evaluation e = opt.get();
		ResponseEntity<?> denied = ActivitesCentreWorkflow.validateSuperviseur(e, user, FEATURE);
		if (denied != null) return denied;
		return ResponseEntity.ok(toRow(repository.save(e)));
	}

	@Transactional
	@PutMapping("/{id}/valider-centrale")
	public ResponseEntity<?> validateCentrale(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		Optional<Evaluation> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		Optional<ResponseEntity<?>> workflowDenied =
				saisieWorkflowGate.transversalValidate(WORKFLOW_RESOURCE, id, FEATURE, user);
		if (workflowDenied.isPresent()) return workflowDenied.get();
		Evaluation e = opt.get();
		ResponseEntity<?> denied = ActivitesCentreWorkflow.validateCentrale(e, user, FEATURE);
		if (denied != null) return denied;
		return ResponseEntity.ok(toRow(repository.save(e)));
	}

	private void apply(Evaluation e, EvaluationRequest r) {
		if (r == null || r.getIdAlpha() == null) {
			throw new IllegalArgumentException("idAlpha est obligatoire");
		}
		e.setIdAlpha(alphaRepository.findById(r.getIdAlpha())
				.orElseThrow(() -> new IllegalArgumentException("Alpha introuvable: " + r.getIdAlpha())));
		NiveauEvaluation niveau = resolveNiveau(r.getIdNiveauEvaluation());
		String typeEvaluation = normalizeTypeEvaluation(r.getTypeEvaluation());
		validateTypeForNiveau(niveau, typeEvaluation);
		e.setIdPeriodeActivite(resolvePeriode(r.getIdPeriodeActivite()));
		e.setIdNiveauEvaluation(niveau);
		e.setTypeEvaluation(typeEvaluation);
		e.setIdTauxEvaluation(resolveTaux(r.getIdTauxEvaluation()));
		if (r.getThemesTaux() != null) {
			replaceThemesTaux(e, r, niveau);
		} else {
			ThemeEvaluation theme = resolveTheme(r.getIdThemeEvaluation());
			validateThemeForNiveau(niveau, theme);
			e.setIdThemeEvaluation(theme);
		}
	}

	private void replaceThemesTaux(Evaluation e, EvaluationRequest r, NiveauEvaluation niveau) {
		if (r.getThemesTaux().isEmpty()) {
			throw new IllegalArgumentException("Au moins un thème avec taux est obligatoire");
		}
		e.getThemesTaux().clear();
		ThemeEvaluation firstTheme = null;
		for (EvaluationRequest.ThemeTauxRequest item : r.getThemesTaux()) {
			if (item == null || item.getIdThemeEvaluation() == null) {
				throw new IllegalArgumentException("Le thème d'évaluation est obligatoire");
			}
			if (item.getTaux() == null || item.getTaux() < 0 || item.getTaux() > 100) {
				throw new IllegalArgumentException("Le taux doit être compris entre 0 et 100");
			}
			ThemeEvaluation theme = resolveTheme(item.getIdThemeEvaluation());
			validateThemeForNiveau(niveau, theme);
			EvaluationThemeTaux row = new EvaluationThemeTaux();
			row.setEvaluation(e);
			row.setThemeEvaluation(theme);
			row.setTaux(item.getTaux());
			e.getThemesTaux().add(row);
			if (firstTheme == null) {
				firstTheme = theme;
			}
		}
		e.setIdThemeEvaluation(firstTheme);
		e.setIdTauxEvaluation(null);
	}

	private PeriodeActivite resolvePeriode(Integer id) {
		if (id == null) return null;
		return periodeRepository.findById(id.longValue())
				.orElseThrow(() -> new IllegalArgumentException("Période d'activité introuvable: " + id));
	}

	private NiveauEvaluation resolveNiveau(Integer id) {
		if (id == null) return null;
		return niveauRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("NiveauEvaluation introuvable: " + id));
	}

	private ThemeEvaluation resolveTheme(Integer id) {
		if (id == null) return null;
		return themeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("ThemeEvaluation introuvable: " + id));
	}

	private TauxEvaluation resolveTaux(Integer id) {
		if (id == null) return null;
		return tauxRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("TauxEvaluation introuvable: " + id));
	}

	private void validateThemeForNiveau(NiveauEvaluation niveau, ThemeEvaluation theme) {
		if (niveau == null || theme == null) {
			return;
		}
		String niveauCode = normalizeNiveau(niveau);
		if (niveauCode == null) {
			return;
		}
		String themeNiveau = normalize(theme.getNiveau());
		if (!themeNiveau.isBlank() && !themeNiveau.equals(niveauCode)) {
			throw new IllegalArgumentException("Ce thème d'évaluation ne correspond pas au niveau sélectionné");
		}
		String themeText = normalize((theme.getCodeThemeEvaluation() == null ? "" : theme.getCodeThemeEvaluation())
				+ " " + (theme.getLibelleThemeEvaluation() == null ? "" : theme.getLibelleThemeEvaluation()));
		if (themeText.contains("FORMATIVE") && !"NIVEAU_1".equals(niveauCode)) {
			throw new IllegalArgumentException("L'évaluation formative concerne uniquement le Niveau 1");
		}
		if (themeText.contains("CERTIFICATIVE") && !("NIVEAU_2".equals(niveauCode) || "POST_ALPHA".equals(niveauCode))) {
			throw new IllegalArgumentException("L'évaluation certificative concerne uniquement le Niveau 2 et Post Alpha");
		}
	}

	private void validateTypeForNiveau(NiveauEvaluation niveau, String typeEvaluation) {
		if (niveau == null || typeEvaluation == null || typeEvaluation.isBlank()) {
			return;
		}
		String niveauCode = normalizeNiveau(niveau);
		if (niveauCode == null) {
			return;
		}
		if ("FORMATIVE".equals(typeEvaluation) && !"NIVEAU_1".equals(niveauCode)) {
			throw new IllegalArgumentException("L'évaluation formative concerne uniquement le Niveau 1");
		}
		if ("CERTIFICATIVE".equals(typeEvaluation) && !("NIVEAU_2".equals(niveauCode) || "POST_ALPHA".equals(niveauCode))) {
			throw new IllegalArgumentException("L'évaluation certificative concerne uniquement le Niveau 2 et Post Alpha");
		}
	}

	private String normalizeTypeEvaluation(String typeEvaluation) {
		if (typeEvaluation == null || typeEvaluation.isBlank()) {
			return null;
		}
		String normalized = normalize(typeEvaluation);
		if (!normalized.equals("FORMATIVE") && !normalized.equals("SOMMATIVE") && !normalized.equals("CERTIFICATIVE")) {
			throw new IllegalArgumentException("Type d'évaluation invalide: " + typeEvaluation);
		}
		return normalized;
	}

	private String normalizeNiveau(NiveauEvaluation niveau) {
		String text = normalize((niveau.getCodeNiveauEvaluation() == null ? "" : niveau.getCodeNiveauEvaluation())
				+ " " + (niveau.getLibelleNiveauEvaluation() == null ? "" : niveau.getLibelleNiveauEvaluation()));
		if (text.contains("POST")) return "POST_ALPHA";
		if (text.contains("2")) return "NIVEAU_2";
		if (text.contains("1")) return "NIVEAU_1";
		return null;
	}

	private String normalize(String value) {
		if (value == null) return "";
		String noAccent = java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "");
		return noAccent.toUpperCase()
				.replaceAll("[^A-Z0-9]+", "_")
				.replaceAll("(^_|_$)", "");
	}

	private Map<String, Object> toRow(Evaluation e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "Alpha", e.getIdAlpha());
		ReferentielEnricher.putRef(m, "PeriodeActivite", e.getIdPeriodeActivite());
		ReferentielEnricher.putRef(m, "NiveauEvaluation", e.getIdNiveauEvaluation());
		ReferentielEnricher.putRef(m, "ThemeEvaluation", e.getIdThemeEvaluation());
		ReferentielEnricher.putRef(m, "TauxEvaluation", e.getIdTauxEvaluation());
		m.put("typeEvaluation", e.getTypeEvaluation());
		ActivitesCentreWorkflow.putStatus(m, e);
		m.put("themesTaux", e.getThemesTaux().stream().map(this::toThemeTauxRow).toList());
		return m;
	}

	private Map<String, Object> toThemeTauxRow(EvaluationThemeTaux item) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", item.getId());
		ReferentielEnricher.putRef(m, "ThemeEvaluation", item.getThemeEvaluation());
		m.put("taux", item.getTaux());
		return m;
	}
}
