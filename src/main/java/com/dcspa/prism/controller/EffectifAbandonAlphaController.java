package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifAbandonAlpha;
import com.dcspa.prism.service.EffectifAbandonAlphaService;
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
@RequestMapping("/api/effectif-abandon-alpha")
@RequiredArgsConstructor
public class EffectifAbandonAlphaController {

	private final EffectifAbandonAlphaService effectifAbandonAlphaService;

	@GetMapping
	public ResponseEntity<List<EffectifAbandonAlpha>> findAll() {
		return ResponseEntity.ok(effectifAbandonAlphaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifAbandonAlpha> findById(@PathVariable Integer id) {
		return effectifAbandonAlphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifAbandonAlpha> create(@RequestBody EffectifAbandonAlpha body) {
		return ResponseEntity.status(201).body(effectifAbandonAlphaService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifAbandonAlpha> update(@PathVariable Integer id, @RequestBody EffectifAbandonAlpha body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifAbandonAlphaService::findById, effectifAbandonAlphaService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifAbandonAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
