package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;
import com.dcspa.prism.entity.TauxEvaluation;
import com.dcspa.prism.repository.TauxEvaluationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/taux-evaluation")
@RequiredArgsConstructor
public class TauxEvaluationController {
	private final TauxEvaluationRepository repository;
	@GetMapping public ResponseEntity<List<TauxEvaluation>> findAll() { return ResponseEntity.ok(repository.findAll()); }
	@GetMapping("/{id}") public ResponseEntity<TauxEvaluation> findById(@PathVariable Integer id) { return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
	@PostMapping public ResponseEntity<TauxEvaluation> create(@RequestBody TauxEvaluation body) { return ResponseEntity.status(201).body(repository.save(body)); }
	@PutMapping("/{id}") public ResponseEntity<TauxEvaluation> update(@PathVariable Integer id, @RequestBody TauxEvaluation body) { return ReferentialPutHelper.putPreservingAutoCode(id, body, repository::findById, repository::save); }
	@DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Integer id) { repository.deleteById(id); return ResponseEntity.noContent().build(); }
	@GetMapping("/search")
	public ResponseEntity<List<TauxEvaluation>> search(@RequestParam(required = false) String code, @RequestParam(required = false) String libelle) {
		String c = code == null ? null : code.toLowerCase();
		String l = libelle == null ? null : libelle.toLowerCase();
		return ResponseEntity.ok(repository.findAll().stream()
				.filter(x -> c == null || (x.getCodeTauxEvaluation() != null && x.getCodeTauxEvaluation().toLowerCase().contains(c)))
				.filter(x -> l == null || (x.getLibelleTauxEvaluation() != null && x.getLibelleTauxEvaluation().toLowerCase().contains(l)))
				.toList());
	}
}
