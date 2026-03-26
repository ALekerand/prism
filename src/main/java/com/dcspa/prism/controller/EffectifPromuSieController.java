package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifPromuSie;
import com.dcspa.prism.service.EffectifPromuSieService;
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
@RequestMapping("/api/effectif-promu-sie")
@RequiredArgsConstructor
public class EffectifPromuSieController {

	private final EffectifPromuSieService effectifPromuSieService;

	@GetMapping
	public ResponseEntity<List<EffectifPromuSie>> findAll() {
		return ResponseEntity.ok(effectifPromuSieService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifPromuSie> findById(@PathVariable Integer id) {
		return effectifPromuSieService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifPromuSie> create(@RequestBody EffectifPromuSie body) {
		return ResponseEntity.status(201).body(effectifPromuSieService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifPromuSie> update(@PathVariable Integer id, @RequestBody EffectifPromuSie body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifPromuSieService::findById, effectifPromuSieService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifPromuSieService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
