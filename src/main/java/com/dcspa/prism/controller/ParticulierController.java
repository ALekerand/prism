package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Particulier;
import com.dcspa.prism.service.ParticulierService;
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
@RequestMapping("/api/particulier")
@RequiredArgsConstructor
public class ParticulierController {

	private final ParticulierService particulierService;

	@GetMapping
	public ResponseEntity<List<Particulier>> findAll() {
		return ResponseEntity.ok(particulierService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Particulier> findById(@PathVariable Integer id) {
		return particulierService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Particulier> create(@RequestBody Particulier body) {
		return ResponseEntity.status(201).body(particulierService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Particulier> update(@PathVariable Integer id, @RequestBody Particulier body) {
		body.setId(id);
		return ResponseEntity.ok(particulierService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		particulierService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
