package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Ministere;
import com.dcspa.prism.service.MinistereService;
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
@RequestMapping("/api/v1/Ministeres")
@RequiredArgsConstructor
public class MinistereController {

	private final MinistereService MinistereService;

	@GetMapping
	public ResponseEntity<List<Ministere>> findAll() {
		List<Ministere> list = MinistereService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Ministere> findById(@PathVariable Integer id) {
		return MinistereService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Ministere> create(@RequestBody Ministere Ministere) {
		Ministere saved = MinistereService.save(Ministere);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Ministere> update(@PathVariable Integer id, @RequestBody Ministere Ministere) {
		Ministere.setId(id);
		Ministere saved = MinistereService.save(Ministere);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		MinistereService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
