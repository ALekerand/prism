package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Commune;
import com.dcspa.prism.service.CommuneService;
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
@RequestMapping("/api/commune")
@RequiredArgsConstructor
public class CommuneController {

	private final CommuneService communeService;

	@GetMapping
	public ResponseEntity<List<Commune>> findAll() {
		return ResponseEntity.ok(communeService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Commune> findById(@PathVariable Integer id) {
		return communeService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Commune> create(@RequestBody Commune body) {
		return ResponseEntity.status(201).body(communeService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Commune> update(@PathVariable Integer id, @RequestBody Commune body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, communeService::findById, communeService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		communeService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
