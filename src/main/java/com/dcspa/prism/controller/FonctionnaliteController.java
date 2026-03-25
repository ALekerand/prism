package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Fonctionnalite;
import com.dcspa.prism.service.FonctionnaliteService;
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
@RequestMapping("/api/fonctionnalite")
@RequiredArgsConstructor
public class FonctionnaliteController {

	private final FonctionnaliteService fonctionnaliteService;

	@GetMapping
	public ResponseEntity<List<Fonctionnalite>> findAll() {
		return ResponseEntity.ok(fonctionnaliteService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Fonctionnalite> findById(@PathVariable Integer id) {
		return fonctionnaliteService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Fonctionnalite> create(@RequestBody Fonctionnalite body) {
		return ResponseEntity.status(201).body(fonctionnaliteService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Fonctionnalite> update(@PathVariable Integer id, @RequestBody Fonctionnalite body) {
		body.setId(id);
		return ResponseEntity.ok(fonctionnaliteService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		fonctionnaliteService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
