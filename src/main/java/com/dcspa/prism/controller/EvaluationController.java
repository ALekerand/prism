package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.EvaluationRequest;
import com.dcspa.prism.entity.Evaluation;
import com.dcspa.prism.entity.NiveauEvaluation;
import com.dcspa.prism.entity.PeriodeEvaluation;
import com.dcspa.prism.entity.TauxEvaluation;
import com.dcspa.prism.entity.ThemeEvaluation;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.EvaluationRepository;
import com.dcspa.prism.repository.NiveauEvaluationRepository;
import com.dcspa.prism.repository.PeriodeEvaluationRepository;
import com.dcspa.prism.repository.TauxEvaluationRepository;
import com.dcspa.prism.repository.ThemeEvaluationRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluation")
@RequiredArgsConstructor
public class EvaluationController {
	private final EvaluationRepository repository;
	private final AlphaRepository alphaRepository;
	private final PeriodeEvaluationRepository periodeRepository;
	private final NiveauEvaluationRepository niveauRepository;
	private final ThemeEvaluationRepository themeRepository;
	private final TauxEvaluationRepository tauxRepository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(repository.findAllWithRefs().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return repository.findByIdWithRefs(id).map(this::toRow).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Transactional(readOnly = true)
	@GetMapping("/search")
	public ResponseEntity<List<Map<String, Object>>> search(
			@RequestParam(required = false) Integer idAlpha,
			@RequestParam(required = false) Integer idPeriodeEvaluation,
			@RequestParam(required = false) Integer idNiveauEvaluation,
			@RequestParam(required = false) Integer idThemeEvaluation,
			@RequestParam(required = false) Integer idTauxEvaluation) {
		return ResponseEntity.ok(repository.findAllWithRefs().stream()
				.filter(x -> idAlpha == null || idAlpha.equals(JpaAssociationIds.intIdOrNull(x.getIdAlpha())))
				.filter(x -> idPeriodeEvaluation == null || idPeriodeEvaluation.equals(JpaAssociationIds.intIdOrNull(x.getIdPeriodeEvaluation())))
				.filter(x -> idNiveauEvaluation == null || idNiveauEvaluation.equals(JpaAssociationIds.intIdOrNull(x.getIdNiveauEvaluation())))
				.filter(x -> idThemeEvaluation == null || idThemeEvaluation.equals(JpaAssociationIds.intIdOrNull(x.getIdThemeEvaluation())))
				.filter(x -> idTauxEvaluation == null || idTauxEvaluation.equals(JpaAssociationIds.intIdOrNull(x.getIdTauxEvaluation())))
				.map(this::toRow).toList());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<?> create(@RequestBody EvaluationRequest body) {
		try {
			Evaluation e = new Evaluation();
			apply(e, body);
			return ResponseEntity.status(201).body(toRow(repository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody EvaluationRequest body) {
		Optional<Evaluation> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		try {
			Evaluation e = opt.get();
			apply(e, body);
			return ResponseEntity.ok(toRow(repository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	private void apply(Evaluation e, EvaluationRequest r) {
		if (r == null || r.getIdAlpha() == null) {
			throw new IllegalArgumentException("idAlpha est obligatoire");
		}
		e.setIdAlpha(alphaRepository.findById(r.getIdAlpha())
				.orElseThrow(() -> new IllegalArgumentException("Alpha introuvable: " + r.getIdAlpha())));
		e.setIdPeriodeEvaluation(resolvePeriode(r.getIdPeriodeEvaluation()));
		e.setIdNiveauEvaluation(resolveNiveau(r.getIdNiveauEvaluation()));
		e.setIdThemeEvaluation(resolveTheme(r.getIdThemeEvaluation()));
		e.setIdTauxEvaluation(resolveTaux(r.getIdTauxEvaluation()));
	}

	private PeriodeEvaluation resolvePeriode(Integer id) {
		if (id == null) return null;
		return periodeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("PeriodeEvaluation introuvable: " + id));
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

	private Map<String, Object> toRow(Evaluation e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "Alpha", e.getIdAlpha());
		ReferentielEnricher.putRef(m, "PeriodeEvaluation", e.getIdPeriodeEvaluation());
		ReferentielEnricher.putRef(m, "NiveauEvaluation", e.getIdNiveauEvaluation());
		ReferentielEnricher.putRef(m, "ThemeEvaluation", e.getIdThemeEvaluation());
		ReferentielEnricher.putRef(m, "TauxEvaluation", e.getIdTauxEvaluation());
		return m;
	}
}
