package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifSie;
import com.dcspa.prism.service.EffectifSieService;
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
@RequestMapping("/api/effectif-sie")
@RequiredArgsConstructor
public class EffectifSieController {

	private final EffectifSieService effectifSieService;

	@GetMapping
	public ResponseEntity<List<EffectifSie>> findAll() {
		return ResponseEntity.ok(effectifSieService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifSie> findById(@PathVariable Integer id) {
		return effectifSieService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifSie> create(@RequestBody EffectifSie body) {
		return ResponseEntity.status(201).body(effectifSieService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifSie> update(@PathVariable Integer id, @RequestBody EffectifSie body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifSieService::findById, effectifSieService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifSieService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
