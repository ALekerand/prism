package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;
import com.dcspa.prism.entity.NiveauEvaluation;
import com.dcspa.prism.repository.NiveauEvaluationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/niveaux-evaluation")
@RequiredArgsConstructor
public class NiveauEvaluationController {
	private final NiveauEvaluationRepository repository;
	@GetMapping public ResponseEntity<List<NiveauEvaluation>> findAll() { return ResponseEntity.ok(repository.findAll()); }
	@GetMapping("/{id}") public ResponseEntity<NiveauEvaluation> findById(@PathVariable Integer id) { return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
	@PostMapping public ResponseEntity<NiveauEvaluation> create(@RequestBody NiveauEvaluation body) { return ResponseEntity.status(201).body(repository.save(body)); }
	@PutMapping("/{id}") public ResponseEntity<NiveauEvaluation> update(@PathVariable Integer id, @RequestBody NiveauEvaluation body) { return ReferentialPutHelper.putPreservingAutoCode(id, body, repository::findById, repository::save); }
	@DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Integer id) { repository.deleteById(id); return ResponseEntity.noContent().build(); }
	@GetMapping("/search")
	public ResponseEntity<List<NiveauEvaluation>> search(@RequestParam(required = false) String code, @RequestParam(required = false) String libelle) {
		String c = code == null ? null : code.toLowerCase();
		String l = libelle == null ? null : libelle.toLowerCase();
		return ResponseEntity.ok(repository.findAll().stream()
				.filter(x -> c == null || (x.getCodeNiveauEvaluation() != null && x.getCodeNiveauEvaluation().toLowerCase().contains(c)))
				.filter(x -> l == null || (x.getLibelleNiveauEvaluation() != null && x.getLibelleNiveauEvaluation().toLowerCase().contains(l)))
				.toList());
	}
}
