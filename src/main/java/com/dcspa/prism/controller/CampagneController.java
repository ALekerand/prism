package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Campagne;
import com.dcspa.prism.service.CampagneService;
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
@RequestMapping("/api/v1/campagnes")
@RequiredArgsConstructor
public class CampagneController {

	private final CampagneService campagneService;

	@GetMapping
	public ResponseEntity<List<Campagne>> findAll() {
		List<Campagne> list = campagneService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Campagne> findById(@PathVariable Integer id) {
		return campagneService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Campagne> create(@RequestBody Campagne campagne) {
		Campagne saved = campagneService.save(campagne);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Campagne> update(@PathVariable Integer id, @RequestBody Campagne campagne) {
		campagne.setId(id);
		Campagne saved = campagneService.save(campagne);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		campagneService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
