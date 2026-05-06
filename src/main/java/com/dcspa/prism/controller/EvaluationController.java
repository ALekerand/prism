package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.dto.EvaluationRequest;
import com.dcspa.prism.entity.*;
import com.dcspa.prism.repository.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluation")
@RequiredArgsConstructor
public class EvaluationController {
	private final EvaluationRepository repository;
	private final AlphaRepository alphaRepository;
	private final PeriodeEvaluationRepository periodeRepository;
	private final NiveauEvaluationRepository niveauRepository;
	private final ThemeEvaluationNiveau1Repository theme1Repository;
	private final ThemeEvaluationNiveau2PostAlphaRepository theme2Repository;
	private final TauxEvaluationRepository tauxRepository;
	private final AspectAAmeliorerRepository aspectRepository;

	@GetMapping public ResponseEntity<List<Map<String, Object>>> findAll() { return ResponseEntity.ok(repository.findAll().stream().map(this::toRow).toList()); }
	@GetMapping("/{id}") public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) { return repository.findById(id).map(this::toRow).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
	@DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Integer id) { repository.deleteById(id); return ResponseEntity.noContent().build(); }

	@GetMapping("/search")
	public ResponseEntity<List<Map<String, Object>>> search(@RequestParam(required = false) Integer idAlpha,
			@RequestParam(required = false) Integer idPeriodeEvaluation,
			@RequestParam(required = false) Integer idNiveauEvaluation,
			@RequestParam(required = false) String themeEvaluation) {
		String t = themeEvaluation == null ? null : themeEvaluation.toLowerCase();
		return ResponseEntity.ok(repository.findAll().stream()
				.filter(x -> idAlpha == null || idAlpha.equals(JpaAssociationIds.intIdOrNull(x.getIdAlpha())))
				.filter(x -> idPeriodeEvaluation == null || idPeriodeEvaluation.equals(JpaAssociationIds.intIdOrNull(x.getIdPeriodeEvaluation())))
				.filter(x -> idNiveauEvaluation == null || idNiveauEvaluation.equals(JpaAssociationIds.intIdOrNull(x.getIdNiveauEvaluation())))
				.filter(x -> t == null || (x.getThemeEvaluation() != null && x.getThemeEvaluation().toLowerCase().contains(t)))
				.map(this::toRow).toList());
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody EvaluationRequest body) { try { Evaluation e = new Evaluation(); apply(e, body); return ResponseEntity.status(201).body(toRow(repository.save(e))); } catch (IllegalArgumentException ex) { return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage())); } }
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody EvaluationRequest body) {
		Optional<Evaluation> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		try { Evaluation e = opt.get(); apply(e, body); return ResponseEntity.ok(toRow(repository.save(e))); } catch (IllegalArgumentException ex) { return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage())); }
	}

	private void apply(Evaluation e, EvaluationRequest r) {
		if (r == null || r.getIdAlpha() == null) throw new IllegalArgumentException("idAlpha est obligatoire");
		e.setIdAlpha(alphaRepository.findById(r.getIdAlpha()).orElseThrow(() -> new IllegalArgumentException("Alpha introuvable: " + r.getIdAlpha())));
		e.setIdPeriodeEvaluation(r.getIdPeriodeEvaluation() == null ? null : periodeRepository.findById(r.getIdPeriodeEvaluation()).orElseThrow(() -> new IllegalArgumentException("PeriodeEvaluation introuvable: " + r.getIdPeriodeEvaluation())));
		e.setIdNiveauEvaluation(r.getIdNiveauEvaluation() == null ? null : niveauRepository.findById(r.getIdNiveauEvaluation()).orElseThrow(() -> new IllegalArgumentException("NiveauEvaluation introuvable: " + r.getIdNiveauEvaluation())));
		e.setIdThemeEvaluationNiveau1(r.getIdThemeEvaluationNiveau1() == null ? null : theme1Repository.findById(r.getIdThemeEvaluationNiveau1()).orElseThrow(() -> new IllegalArgumentException("ThemeNiveau1 introuvable: " + r.getIdThemeEvaluationNiveau1())));
		e.setIdThemeEvaluationN2PostAlpha(r.getIdThemeEvaluationN2PostAlpha() == null ? null : theme2Repository.findById(r.getIdThemeEvaluationN2PostAlpha()).orElseThrow(() -> new IllegalArgumentException("ThemeNiveau2 introuvable: " + r.getIdThemeEvaluationN2PostAlpha())));
		e.setIdTauxEvaluation(r.getIdTauxEvaluation() == null ? null : tauxRepository.findById(r.getIdTauxEvaluation()).orElseThrow(() -> new IllegalArgumentException("TauxEvaluation introuvable: " + r.getIdTauxEvaluation())));
		e.setIdAspectAAmeliorer(r.getIdAspectAAmeliorer() == null ? null : aspectRepository.findById(r.getIdAspectAAmeliorer()).orElseThrow(() -> new IllegalArgumentException("AspectAAmeliorer introuvable: " + r.getIdAspectAAmeliorer())));
		e.setThemeEvaluation(r.getThemeEvaluation());
	}

	private Map<String, Object> toRow(Evaluation e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		m.put("idAlpha", JpaAssociationIds.intIdOrNull(e.getIdAlpha()));
		m.put("idPeriodeEvaluation", JpaAssociationIds.intIdOrNull(e.getIdPeriodeEvaluation()));
		m.put("idNiveauEvaluation", JpaAssociationIds.intIdOrNull(e.getIdNiveauEvaluation()));
		m.put("idThemeEvaluationNiveau1", JpaAssociationIds.intIdOrNull(e.getIdThemeEvaluationNiveau1()));
		m.put("idThemeEvaluationN2PostAlpha", JpaAssociationIds.intIdOrNull(e.getIdThemeEvaluationN2PostAlpha()));
		m.put("idTauxEvaluation", JpaAssociationIds.intIdOrNull(e.getIdTauxEvaluation()));
		m.put("idAspectAAmeliorer", JpaAssociationIds.intIdOrNull(e.getIdAspectAAmeliorer()));
		m.put("themeEvaluation", e.getThemeEvaluation());
		return m;
	}
}
