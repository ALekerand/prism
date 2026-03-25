package com.dcspa.prism.controller;

import com.dcspa.prism.entity.DrenaDepartement;
import com.dcspa.prism.service.DrenaDepartementService;
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
@RequestMapping("/api/drena-departement")
@RequiredArgsConstructor
public class DrenaDepartementController {

	private final DrenaDepartementService drenaDepartementService;

	@GetMapping
	public ResponseEntity<List<DrenaDepartement>> findAll() {
		return ResponseEntity.ok(drenaDepartementService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<DrenaDepartement> findById(@PathVariable Integer id) {
		return drenaDepartementService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<DrenaDepartement> create(@RequestBody DrenaDepartement body) {
		return ResponseEntity.status(201).body(drenaDepartementService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<DrenaDepartement> update(@PathVariable Integer id, @RequestBody DrenaDepartement body) {
		body.setId(id);
		return ResponseEntity.ok(drenaDepartementService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		drenaDepartementService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
