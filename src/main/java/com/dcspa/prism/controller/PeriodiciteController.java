package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Periodicite;
import com.dcspa.prism.service.PeriodiciteService;
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
@RequestMapping("/api/v1/Periodicites")
@RequiredArgsConstructor
public class PeriodiciteController {

	private final PeriodiciteService PeriodiciteService;

	@GetMapping
	public ResponseEntity<List<Periodicite>> findAll() {
		List<Periodicite> list = PeriodiciteService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Periodicite> findById(@PathVariable Integer id) {
		return PeriodiciteService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Periodicite> create(@RequestBody Periodicite Periodicite) {
		Periodicite saved = PeriodiciteService.save(Periodicite);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Periodicite> update(@PathVariable Integer id, @RequestBody Periodicite Periodicite) {
		Periodicite.setId(id);
		Periodicite saved = PeriodiciteService.save(Periodicite);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		PeriodiciteService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
