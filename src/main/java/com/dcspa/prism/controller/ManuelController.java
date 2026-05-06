package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;
import com.dcspa.prism.entity.Manuel;
import com.dcspa.prism.repository.ManuelRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manuels")
@RequiredArgsConstructor
public class ManuelController {
	private final ManuelRepository repository;

	@GetMapping public ResponseEntity<List<Manuel>> findAll() { return ResponseEntity.ok(repository.findAll()); }
	@GetMapping("/{id}") public ResponseEntity<Manuel> findById(@PathVariable Integer id) { return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
	@PostMapping public ResponseEntity<Manuel> create(@RequestBody Manuel body) { return ResponseEntity.status(201).body(repository.save(body)); }
	@PutMapping("/{id}") public ResponseEntity<Manuel> update(@PathVariable Integer id, @RequestBody Manuel body) { return ReferentialPutHelper.putPreservingAutoCode(id, body, repository::findById, repository::save); }
	@DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Integer id) { repository.deleteById(id); return ResponseEntity.noContent().build(); }

	@GetMapping("/search")
	public ResponseEntity<List<Manuel>> search(@RequestParam(required = false) String code, @RequestParam(required = false) String libelle) {
		String c = code == null ? null : code.toLowerCase();
		String l = libelle == null ? null : libelle.toLowerCase();
		return ResponseEntity.ok(repository.findAll().stream()
				.filter(x -> c == null || (x.getCodeManuel() != null && x.getCodeManuel().toLowerCase().contains(c)))
				.filter(x -> l == null || (x.getLibelleManuel() != null && x.getLibelleManuel().toLowerCase().contains(l)))
				.toList());
	}
}
