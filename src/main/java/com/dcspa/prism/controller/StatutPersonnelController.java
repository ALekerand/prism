package com.dcspa.prism.controller;

import com.dcspa.prism.entity.StatutPersonnel;
import com.dcspa.prism.service.StatutPersonnelService;
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
@RequestMapping("/api/v1/StatutPersonnels")
@RequiredArgsConstructor
public class StatutPersonnelController {

	private final StatutPersonnelService StatutPersonnelService;

	@GetMapping
	public ResponseEntity<List<StatutPersonnel>> findAll() {
		List<StatutPersonnel> list = StatutPersonnelService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<StatutPersonnel> findById(@PathVariable Integer id) {
		return StatutPersonnelService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<StatutPersonnel> create(@RequestBody StatutPersonnel StatutPersonnel) {
		StatutPersonnel saved = StatutPersonnelService.save(StatutPersonnel);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<StatutPersonnel> update(@PathVariable Integer id, @RequestBody StatutPersonnel StatutPersonnel) {
		StatutPersonnel.setId(id);
		StatutPersonnel saved = StatutPersonnelService.save(StatutPersonnel);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		StatutPersonnelService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
