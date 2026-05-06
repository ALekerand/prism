package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;
import com.dcspa.prism.entity.PeriodeEvaluation;
import com.dcspa.prism.repository.PeriodeEvaluationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/periodes-evaluation")
@RequiredArgsConstructor
public class PeriodeEvaluationController {
	private final PeriodeEvaluationRepository repository;
	@GetMapping public ResponseEntity<List<PeriodeEvaluation>> findAll() { return ResponseEntity.ok(repository.findAll()); }
	@GetMapping("/{id}") public ResponseEntity<PeriodeEvaluation> findById(@PathVariable Integer id) { return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
	@PostMapping public ResponseEntity<PeriodeEvaluation> create(@RequestBody PeriodeEvaluation body) { return ResponseEntity.status(201).body(repository.save(body)); }
	@PutMapping("/{id}") public ResponseEntity<PeriodeEvaluation> update(@PathVariable Integer id, @RequestBody PeriodeEvaluation body) { return ReferentialPutHelper.putPreservingAutoCode(id, body, repository::findById, repository::save); }
	@DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Integer id) { repository.deleteById(id); return ResponseEntity.noContent().build(); }
	@GetMapping("/search")
	public ResponseEntity<List<PeriodeEvaluation>> search(@RequestParam(required = false) String code, @RequestParam(required = false) String libelle) {
		String c = code == null ? null : code.toLowerCase();
		String l = libelle == null ? null : libelle.toLowerCase();
		return ResponseEntity.ok(repository.findAll().stream()
				.filter(x -> c == null || (x.getCodePeriodeEvaluation() != null && x.getCodePeriodeEvaluation().toLowerCase().contains(c)))
				.filter(x -> l == null || (x.getLibellePeriodeEvaluation() != null && x.getLibellePeriodeEvaluation().toLowerCase().contains(l)))
				.toList());
	}
}
