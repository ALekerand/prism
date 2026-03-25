package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Drena;
import com.dcspa.prism.service.DrenaService;
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
@RequestMapping("/api/drena")
@RequiredArgsConstructor
public class DrenaController {

	private final DrenaService drenaService;

	@GetMapping
	public ResponseEntity<List<Drena>> findAll() {
		return ResponseEntity.ok(drenaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Drena> findById(@PathVariable Integer id) {
		return drenaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Drena> create(@RequestBody Drena body) {
		return ResponseEntity.status(201).body(drenaService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Drena> update(@PathVariable Integer id, @RequestBody Drena body) {
		body.setId(id);
		return ResponseEntity.ok(drenaService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		drenaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
