package com.dcspa.prism.controller;

import com.dcspa.prism.entity.DomaineActivite;
import com.dcspa.prism.service.DomaineActiviteService;
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
@RequestMapping("/api/domaine-activite")
@RequiredArgsConstructor
public class DomaineActiviteController {

	private final DomaineActiviteService domaineActiviteService;

	@GetMapping
	public ResponseEntity<List<DomaineActivite>> findAll() {
		return ResponseEntity.ok(domaineActiviteService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<DomaineActivite> findById(@PathVariable Integer id) {
		return domaineActiviteService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<DomaineActivite> create(@RequestBody DomaineActivite body) {
		return ResponseEntity.status(201).body(domaineActiviteService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<DomaineActivite> update(@PathVariable Integer id, @RequestBody DomaineActivite body) {
		body.setId(id);
		return ResponseEntity.ok(domaineActiviteService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		domaineActiviteService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
