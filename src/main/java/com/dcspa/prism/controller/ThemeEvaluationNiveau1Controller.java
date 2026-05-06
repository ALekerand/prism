package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;
import com.dcspa.prism.entity.ThemeEvaluationNiveau1;
import com.dcspa.prism.repository.ThemeEvaluationNiveau1Repository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/themes-evaluation-niveau1")
@RequiredArgsConstructor
public class ThemeEvaluationNiveau1Controller {
	private final ThemeEvaluationNiveau1Repository repository;
	@GetMapping public ResponseEntity<List<ThemeEvaluationNiveau1>> findAll() { return ResponseEntity.ok(repository.findAll()); }
	@GetMapping("/{id}") public ResponseEntity<ThemeEvaluationNiveau1> findById(@PathVariable Integer id) { return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
	@PostMapping public ResponseEntity<ThemeEvaluationNiveau1> create(@RequestBody ThemeEvaluationNiveau1 body) { return ResponseEntity.status(201).body(repository.save(body)); }
	@PutMapping("/{id}") public ResponseEntity<ThemeEvaluationNiveau1> update(@PathVariable Integer id, @RequestBody ThemeEvaluationNiveau1 body) { return ReferentialPutHelper.putPreservingAutoCode(id, body, repository::findById, repository::save); }
	@DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Integer id) { repository.deleteById(id); return ResponseEntity.noContent().build(); }
	@GetMapping("/search")
	public ResponseEntity<List<ThemeEvaluationNiveau1>> search(@RequestParam(required = false) String code, @RequestParam(required = false) String libelle) {
		String c = code == null ? null : code.toLowerCase();
		String l = libelle == null ? null : libelle.toLowerCase();
		return ResponseEntity.ok(repository.findAll().stream()
				.filter(x -> c == null || (x.getCodeThemeEvaluationNiveau1() != null && x.getCodeThemeEvaluationNiveau1().toLowerCase().contains(c)))
				.filter(x -> l == null || (x.getLibelleThemeEvaluationNiveau1() != null && x.getLibelleThemeEvaluationNiveau1().toLowerCase().contains(l)))
				.toList());
	}
}
