package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Partenaire;
import com.dcspa.prism.service.PartenaireService;
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
@RequestMapping("/api/v1/Partenaires")
@RequiredArgsConstructor
public class PartenaireController {

	private final PartenaireService PartenaireService;

	@GetMapping
	public ResponseEntity<List<Partenaire>> findAll() {
		List<Partenaire> list = PartenaireService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Partenaire> findById(@PathVariable Integer id) {
		return PartenaireService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Partenaire> create(@RequestBody Partenaire Partenaire) {
		Partenaire saved = PartenaireService.save(Partenaire);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Partenaire> update(@PathVariable Integer id, @RequestBody Partenaire Partenaire) {
		Partenaire.setId(id);
		Partenaire saved = PartenaireService.save(Partenaire);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		PartenaireService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
