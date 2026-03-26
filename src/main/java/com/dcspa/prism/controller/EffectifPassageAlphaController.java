package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifPassageAlpha;
import com.dcspa.prism.service.EffectifPassageAlphaService;
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
@RequestMapping("/api/effectif-passage-alpha")
@RequiredArgsConstructor
public class EffectifPassageAlphaController {

	private final EffectifPassageAlphaService effectifPassageAlphaService;

	@GetMapping
	public ResponseEntity<List<EffectifPassageAlpha>> findAll() {
		return ResponseEntity.ok(effectifPassageAlphaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifPassageAlpha> findById(@PathVariable Integer id) {
		return effectifPassageAlphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifPassageAlpha> create(@RequestBody EffectifPassageAlpha body) {
		return ResponseEntity.status(201).body(effectifPassageAlphaService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifPassageAlpha> update(@PathVariable Integer id, @RequestBody EffectifPassageAlpha body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifPassageAlphaService::findById, effectifPassageAlphaService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifPassageAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
