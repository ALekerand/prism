package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Difficulte;
import com.dcspa.prism.service.DifficulteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/difficulte")
@RequiredArgsConstructor
public class DifficulteController {

	private final DifficulteService difficulteService;

	@GetMapping
	public ResponseEntity<List<Difficulte>> findAll() {
		return ResponseEntity.ok(difficulteService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Difficulte> findById(@PathVariable Integer id) {
		return difficulteService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Difficulte> create(@RequestBody Difficulte body) {
		return ResponseEntity.status(201).body(difficulteService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Difficulte> update(@PathVariable Integer id, @RequestBody Difficulte body) {
		body.setId(id);
		return ResponseEntity.ok(difficulteService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		difficulteService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
