package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.service.CentreService;
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
@RequestMapping("/api/v1/centres")
@RequiredArgsConstructor
public class CentreController {

	private final CentreService centreService;

	@GetMapping
	public ResponseEntity<List<Centre>> findAll() {
		List<Centre> list = centreService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Centre> findById(@PathVariable Integer id) {
		return centreService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Centre> create(@RequestBody Centre centre) {
		Centre saved = centreService.save(centre);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Centre> update(@PathVariable Integer id, @RequestBody Centre centre) {
		centre.setId(id);
		Centre saved = centreService.save(centre);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		centreService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
