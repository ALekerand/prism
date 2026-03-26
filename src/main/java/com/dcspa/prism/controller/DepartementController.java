package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Departement;
import com.dcspa.prism.service.DepartementService;
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
@RequestMapping("/api/departement")
@RequiredArgsConstructor
public class DepartementController {

	private final DepartementService departementService;

	@GetMapping
	public ResponseEntity<List<Departement>> findAll() {
		return ResponseEntity.ok(departementService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Departement> findById(@PathVariable Integer id) {
		return departementService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Departement> create(@RequestBody Departement body) {
		return ResponseEntity.status(201).body(departementService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Departement> update(@PathVariable Integer id, @RequestBody Departement body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, departementService::findById, departementService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		departementService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
