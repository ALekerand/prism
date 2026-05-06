package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;
import com.dcspa.prism.entity.Discipline;
import com.dcspa.prism.repository.DisciplineRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/disciplines")
@RequiredArgsConstructor
public class DisciplineController {
	private final DisciplineRepository repository;

	@GetMapping
	public ResponseEntity<List<Discipline>> findAll() {
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping("/search")
	public ResponseEntity<List<Discipline>> search(
			@RequestParam(required = false) String code,
			@RequestParam(required = false) String libelle) {
		String c = code == null ? null : code.toLowerCase();
		String l = libelle == null ? null : libelle.toLowerCase();
		return ResponseEntity.ok(repository.findAll().stream()
				.filter(x -> c == null || (x.getCodeDiscipline() != null && x.getCodeDiscipline().toLowerCase().contains(c)))
				.filter(x -> l == null || (x.getLibelleDiscipline() != null && x.getLibelleDiscipline().toLowerCase().contains(l)))
				.toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Discipline> findById(@PathVariable Integer id) {
		return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Discipline> create(@RequestBody Discipline body) {
		return ResponseEntity.status(201).body(repository.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Discipline> update(@PathVariable Integer id, @RequestBody Discipline body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, repository::findById, repository::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
