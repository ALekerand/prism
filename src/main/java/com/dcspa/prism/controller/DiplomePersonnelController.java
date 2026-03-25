package com.dcspa.prism.controller;

import com.dcspa.prism.entity.DiplomePersonnel;
import com.dcspa.prism.service.DiplomePersonnelService;
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
@RequestMapping("/api/diplome-personnel")
@RequiredArgsConstructor
public class DiplomePersonnelController {

	private final DiplomePersonnelService diplomePersonnelService;

	@GetMapping
	public ResponseEntity<List<DiplomePersonnel>> findAll() {
		return ResponseEntity.ok(diplomePersonnelService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<DiplomePersonnel> findById(@PathVariable Integer id) {
		return diplomePersonnelService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<DiplomePersonnel> create(@RequestBody DiplomePersonnel body) {
		return ResponseEntity.status(201).body(diplomePersonnelService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<DiplomePersonnel> update(@PathVariable Integer id, @RequestBody DiplomePersonnel body) {
		body.setId(id);
		return ResponseEntity.ok(diplomePersonnelService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		diplomePersonnelService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
