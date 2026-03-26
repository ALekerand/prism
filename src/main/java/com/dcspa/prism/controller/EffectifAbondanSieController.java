package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifAbondanSie;
import com.dcspa.prism.service.EffectifAbondanSieService;
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
@RequestMapping("/api/effectif-abondan-sie")
@RequiredArgsConstructor
public class EffectifAbondanSieController {

	private final EffectifAbondanSieService effectifAbondanSieService;

	@GetMapping
	public ResponseEntity<List<EffectifAbondanSie>> findAll() {
		return ResponseEntity.ok(effectifAbondanSieService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifAbondanSie> findById(@PathVariable Integer id) {
		return effectifAbondanSieService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifAbondanSie> create(@RequestBody EffectifAbondanSie body) {
		return ResponseEntity.status(201).body(effectifAbondanSieService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifAbondanSie> update(@PathVariable Integer id, @RequestBody EffectifAbondanSie body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifAbondanSieService::findById, effectifAbondanSieService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifAbondanSieService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
