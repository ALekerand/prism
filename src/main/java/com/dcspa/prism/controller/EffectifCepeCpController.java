package com.dcspa.prism.controller;

import com.dcspa.prism.entity.EffectifCepeCp;
import com.dcspa.prism.service.EffectifCepeCpService;
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
@RequestMapping("/api/effectif-cepe-cp")
@RequiredArgsConstructor
public class EffectifCepeCpController {

	private final EffectifCepeCpService effectifCepeCpService;

	@GetMapping
	public ResponseEntity<List<EffectifCepeCp>> findAll() {
		return ResponseEntity.ok(effectifCepeCpService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifCepeCp> findById(@PathVariable Integer id) {
		return effectifCepeCpService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifCepeCp> create(@RequestBody EffectifCepeCp body) {
		return ResponseEntity.status(201).body(effectifCepeCpService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifCepeCp> update(@PathVariable Integer id, @RequestBody EffectifCepeCp body) {
		body.setId(id);
		return ResponseEntity.ok(effectifCepeCpService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifCepeCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
