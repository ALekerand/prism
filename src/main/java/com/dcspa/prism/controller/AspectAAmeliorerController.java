package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;
import com.dcspa.prism.entity.AspectAAmeliorer;
import com.dcspa.prism.repository.AspectAAmeliorerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aspects-a-ameliorer")
@RequiredArgsConstructor
public class AspectAAmeliorerController {
	private final AspectAAmeliorerRepository repository;
	@GetMapping public ResponseEntity<List<AspectAAmeliorer>> findAll() { return ResponseEntity.ok(repository.findAll()); }
	@GetMapping("/{id}") public ResponseEntity<AspectAAmeliorer> findById(@PathVariable Integer id) { return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
	@PostMapping public ResponseEntity<AspectAAmeliorer> create(@RequestBody AspectAAmeliorer body) { return ResponseEntity.status(201).body(repository.save(body)); }
	@PutMapping("/{id}") public ResponseEntity<AspectAAmeliorer> update(@PathVariable Integer id, @RequestBody AspectAAmeliorer body) { return ReferentialPutHelper.putPreservingAutoCode(id, body, repository::findById, repository::save); }
	@DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Integer id) { repository.deleteById(id); return ResponseEntity.noContent().build(); }
	@GetMapping("/search")
	public ResponseEntity<List<AspectAAmeliorer>> search(@RequestParam(required = false) String code, @RequestParam(required = false) String libelle) {
		String c = code == null ? null : code.toLowerCase();
		String l = libelle == null ? null : libelle.toLowerCase();
		return ResponseEntity.ok(repository.findAll().stream()
				.filter(x -> c == null || (x.getCodeAspectAAmeliorer() != null && x.getCodeAspectAAmeliorer().toLowerCase().contains(c)))
				.filter(x -> l == null || (x.getLibelleAspectAAmeliorer() != null && x.getLibelleAspectAAmeliorer().toLowerCase().contains(l)))
				.toList());
	}
}
