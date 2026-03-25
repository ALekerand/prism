package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Impact;
import com.dcspa.prism.service.ImpactService;
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
@RequestMapping("/api/impact")
@RequiredArgsConstructor
public class ImpactController {

	private final ImpactService impactService;

	@GetMapping
	public ResponseEntity<List<Impact>> findAll() {
		return ResponseEntity.ok(impactService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Impact> findById(@PathVariable Integer id) {
		return impactService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Impact> create(@RequestBody Impact body) {
		return ResponseEntity.status(201).body(impactService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Impact> update(@PathVariable Integer id, @RequestBody Impact body) {
		body.setId(id);
		return ResponseEntity.ok(impactService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		impactService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
