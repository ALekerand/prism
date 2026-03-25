package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Designation;
import com.dcspa.prism.service.DesignationService;
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
@RequestMapping("/api/designation")
@RequiredArgsConstructor
public class DesignationController {

	private final DesignationService designationService;

	@GetMapping
	public ResponseEntity<List<Designation>> findAll() {
		return ResponseEntity.ok(designationService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Designation> findById(@PathVariable Integer id) {
		return designationService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Designation> create(@RequestBody Designation body) {
		return ResponseEntity.status(201).body(designationService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Designation> update(@PathVariable Integer id, @RequestBody Designation body) {
		body.setId(id);
		return ResponseEntity.ok(designationService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		designationService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
