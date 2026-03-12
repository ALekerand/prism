package com.dcspa.prism.controller;

import com.dcspa.prism.entity.LangueApprentissage;
import com.dcspa.prism.service.LangueApprentissageService;
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
@RequestMapping("/api/v1/LangueApprentissages")
@RequiredArgsConstructor
public class LangueApprentissageController {

	private final LangueApprentissageService LangueApprentissageService;

	@GetMapping
	public ResponseEntity<List<LangueApprentissage>> findAll() {
		List<LangueApprentissage> list = LangueApprentissageService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LangueApprentissage> findById(@PathVariable Integer id) {
		return LangueApprentissageService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<LangueApprentissage> create(@RequestBody LangueApprentissage LangueApprentissage) {
		LangueApprentissage saved = LangueApprentissageService.save(LangueApprentissage);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<LangueApprentissage> update(@PathVariable Integer id, @RequestBody LangueApprentissage LangueApprentissage) {
		LangueApprentissage.setId(id);
		LangueApprentissage saved = LangueApprentissageService.save(LangueApprentissage);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		LangueApprentissageService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}

