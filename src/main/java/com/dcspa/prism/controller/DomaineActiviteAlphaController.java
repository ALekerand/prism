package com.dcspa.prism.controller;

import com.dcspa.prism.entity.DomaineActiviteAlpha;
import com.dcspa.prism.service.DomaineActiviteAlphaService;
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
@RequestMapping("/api/domaine-activite-alpha")
@RequiredArgsConstructor
public class DomaineActiviteAlphaController {

	private final DomaineActiviteAlphaService domaineActiviteAlphaService;

	@GetMapping
	public ResponseEntity<List<DomaineActiviteAlpha>> findAll() {
		return ResponseEntity.ok(domaineActiviteAlphaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<DomaineActiviteAlpha> findById(@PathVariable Integer id) {
		return domaineActiviteAlphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<DomaineActiviteAlpha> create(@RequestBody DomaineActiviteAlpha body) {
		return ResponseEntity.status(201).body(domaineActiviteAlphaService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<DomaineActiviteAlpha> update(@PathVariable Integer id, @RequestBody DomaineActiviteAlpha body) {
		body.setId(id);
		return ResponseEntity.ok(domaineActiviteAlphaService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		domaineActiviteAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
