package com.dcspa.prism.controller;

import com.dcspa.prism.entity.ImpactAlpha;
import com.dcspa.prism.service.ImpactAlphaService;
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
@RequestMapping("/api/impact-alpha")
@RequiredArgsConstructor
public class ImpactAlphaController {

	private final ImpactAlphaService impactAlphaService;

	@GetMapping
	public ResponseEntity<List<ImpactAlpha>> findAll() {
		return ResponseEntity.ok(impactAlphaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ImpactAlpha> findById(@PathVariable Integer id) {
		return impactAlphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<ImpactAlpha> create(@RequestBody ImpactAlpha body) {
		return ResponseEntity.status(201).body(impactAlphaService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ImpactAlpha> update(@PathVariable Integer id, @RequestBody ImpactAlpha body) {
		body.setId(id);
		return ResponseEntity.ok(impactAlphaService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		impactAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
