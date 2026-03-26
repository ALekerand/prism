package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.service.PromoteurService;
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
@RequestMapping("/api/promoteur")
@RequiredArgsConstructor
public class PromoteurController {

	private final PromoteurService promoteurService;

	@GetMapping
	public ResponseEntity<List<Promoteur>> findAll() {
		return ResponseEntity.ok(promoteurService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Promoteur> findById(@PathVariable Integer id) {
		return promoteurService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Promoteur> create(@RequestBody Promoteur body) {
		return ResponseEntity.status(201).body(promoteurService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Promoteur> update(@PathVariable Integer id, @RequestBody Promoteur body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, promoteurService::findById, promoteurService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		promoteurService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
