package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.service.CpService;
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
@RequestMapping("/api/cp")
@RequiredArgsConstructor
public class CpController {

	private final CpService cpService;

	@GetMapping
	public ResponseEntity<List<Cp>> findAll() {
		return ResponseEntity.ok(cpService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cp> findById(@PathVariable Integer id) {
		return cpService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Cp> create(@RequestBody Cp body) {
		return ResponseEntity.status(201).body(cpService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cp> update(@PathVariable Integer id, @RequestBody Cp body) {
		body.setId(id);
		return ResponseEntity.ok(cpService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		cpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
