package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;
import com.dcspa.prism.entity.ThemeEvaluationNiveau2PostAlpha;
import com.dcspa.prism.repository.ThemeEvaluationNiveau2PostAlphaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/themes-evaluation-niveau2-post-alpha")
@RequiredArgsConstructor
public class ThemeEvaluationNiveau2PostAlphaController {
	private final ThemeEvaluationNiveau2PostAlphaRepository repository;
	@GetMapping public ResponseEntity<List<ThemeEvaluationNiveau2PostAlpha>> findAll() { return ResponseEntity.ok(repository.findAll()); }
	@GetMapping("/{id}") public ResponseEntity<ThemeEvaluationNiveau2PostAlpha> findById(@PathVariable Integer id) { return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
	@PostMapping public ResponseEntity<ThemeEvaluationNiveau2PostAlpha> create(@RequestBody ThemeEvaluationNiveau2PostAlpha body) { return ResponseEntity.status(201).body(repository.save(body)); }
	@PutMapping("/{id}") public ResponseEntity<ThemeEvaluationNiveau2PostAlpha> update(@PathVariable Integer id, @RequestBody ThemeEvaluationNiveau2PostAlpha body) { return ReferentialPutHelper.putPreservingAutoCode(id, body, repository::findById, repository::save); }
	@DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Integer id) { repository.deleteById(id); return ResponseEntity.noContent().build(); }
	@GetMapping("/search")
	public ResponseEntity<List<ThemeEvaluationNiveau2PostAlpha>> search(@RequestParam(required = false) String code, @RequestParam(required = false) String libelle) {
		String c = code == null ? null : code.toLowerCase();
		String l = libelle == null ? null : libelle.toLowerCase();
		return ResponseEntity.ok(repository.findAll().stream()
				.filter(x -> c == null || (x.getCodeThemeEvaluationNiveau2PostAlpha() != null && x.getCodeThemeEvaluationNiveau2PostAlpha().toLowerCase().contains(c)))
				.filter(x -> l == null || (x.getLibelleThemeEvaluationNiveau2PostAlpha() != null && x.getLibelleThemeEvaluationNiveau2PostAlpha().toLowerCase().contains(l)))
				.toList());
	}
}
