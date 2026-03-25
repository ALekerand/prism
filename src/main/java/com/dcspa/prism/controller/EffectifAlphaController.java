package com.dcspa.prism.controller;

import com.dcspa.prism.entity.EffectifAlpha;
import com.dcspa.prism.service.EffectifAlphaService;
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
@RequestMapping("/api/effectif-alpha")
@RequiredArgsConstructor
public class EffectifAlphaController {

	private final EffectifAlphaService effectifAlphaService;

	@GetMapping
	public ResponseEntity<List<EffectifAlpha>> findAll() {
		return ResponseEntity.ok(effectifAlphaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifAlpha> findById(@PathVariable Integer id) {
		return effectifAlphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifAlpha> create(@RequestBody EffectifAlpha body) {
		return ResponseEntity.status(201).body(effectifAlphaService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifAlpha> update(@PathVariable Integer id, @RequestBody EffectifAlpha body) {
		body.setId(id);
		return ResponseEntity.ok(effectifAlphaService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
