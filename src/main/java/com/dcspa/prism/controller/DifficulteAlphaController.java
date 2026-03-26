package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.DifficulteAlpha;
import com.dcspa.prism.service.DifficulteAlphaService;
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
@RequestMapping("/api/difficulte-alpha")
@RequiredArgsConstructor
public class DifficulteAlphaController {

	private final DifficulteAlphaService difficulteAlphaService;

	@GetMapping
	public ResponseEntity<List<DifficulteAlpha>> findAll() {
		return ResponseEntity.ok(difficulteAlphaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<DifficulteAlpha> findById(@PathVariable Integer id) {
		return difficulteAlphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<DifficulteAlpha> create(@RequestBody DifficulteAlpha body) {
		return ResponseEntity.status(201).body(difficulteAlphaService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<DifficulteAlpha> update(@PathVariable Integer id, @RequestBody DifficulteAlpha body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, difficulteAlphaService::findById, difficulteAlphaService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		difficulteAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
