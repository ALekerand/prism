package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;
import com.dcspa.prism.entity.NiveauControle;
import com.dcspa.prism.repository.NiveauControleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/niveaux-controle")
@RequiredArgsConstructor
public class NiveauControleController {
	private final NiveauControleRepository repository;

	@GetMapping public ResponseEntity<List<NiveauControle>> findAll() { return ResponseEntity.ok(repository.findAll()); }
	@GetMapping("/{id}") public ResponseEntity<NiveauControle> findById(@PathVariable Integer id) { return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
	@PostMapping public ResponseEntity<NiveauControle> create(@RequestBody NiveauControle body) { return ResponseEntity.status(201).body(repository.save(body)); }
	@PutMapping("/{id}") public ResponseEntity<NiveauControle> update(@PathVariable Integer id, @RequestBody NiveauControle body) { return ReferentialPutHelper.putPreservingAutoCode(id, body, repository::findById, repository::save); }
	@DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Integer id) { repository.deleteById(id); return ResponseEntity.noContent().build(); }

	@GetMapping("/search")
	public ResponseEntity<List<NiveauControle>> search(@RequestParam(required = false) String code, @RequestParam(required = false) String libelle) {
		String c = code == null ? null : code.toLowerCase();
		String l = libelle == null ? null : libelle.toLowerCase();
		return ResponseEntity.ok(repository.findAll().stream()
				.filter(x -> c == null || (x.getCodeNiveauControle() != null && x.getCodeNiveauControle().toLowerCase().contains(c)))
				.filter(x -> l == null || (x.getLibelleNiveauControle() != null && x.getLibelleNiveauControle().toLowerCase().contains(l)))
				.toList());
	}
}
