package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Diplome;
import com.dcspa.prism.service.DiplomeService;
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
@RequestMapping("/api/diplome")
@RequiredArgsConstructor
public class DiplomeController {

	private final DiplomeService diplomeService;

	@GetMapping
	public ResponseEntity<List<Diplome>> findAll() {
		return ResponseEntity.ok(diplomeService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Diplome> findById(@PathVariable Integer id) {
		return diplomeService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Diplome> create(@RequestBody Diplome body) {
		return ResponseEntity.status(201).body(diplomeService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Diplome> update(@PathVariable Integer id, @RequestBody Diplome body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, diplomeService::findById, diplomeService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		diplomeService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
