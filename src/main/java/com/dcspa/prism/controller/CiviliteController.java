package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Civilite;
import com.dcspa.prism.service.CiviliteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/civilite")
@RequiredArgsConstructor
public class CiviliteController {

	private final CiviliteService civiliteService;

	@GetMapping
	public ResponseEntity<List<Civilite>> findAll() {
		List<Civilite> list = civiliteService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Civilite> findById(@PathVariable Integer id) {
		return civiliteService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Civilite> create(@RequestBody Civilite civilite) {
		Civilite saved = civiliteService.save(civilite);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Civilite> update(@PathVariable Integer id, @RequestBody Civilite civilite) {
		civilite.setId(id);
		Civilite saved = civiliteService.save(civilite);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		civiliteService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
