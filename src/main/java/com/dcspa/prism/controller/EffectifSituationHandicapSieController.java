package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifSituationHandicapSie;
import com.dcspa.prism.service.EffectifSituationHandicapSieService;
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
@RequestMapping("/api/effectif-situation-handicap-sie")
@RequiredArgsConstructor
public class EffectifSituationHandicapSieController {

	private final EffectifSituationHandicapSieService effectifSituationHandicapSieService;

	@GetMapping
	public ResponseEntity<List<EffectifSituationHandicapSie>> findAll() {
		return ResponseEntity.ok(effectifSituationHandicapSieService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifSituationHandicapSie> findById(@PathVariable Integer id) {
		return effectifSituationHandicapSieService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifSituationHandicapSie> create(@RequestBody EffectifSituationHandicapSie body) {
		return ResponseEntity.status(201).body(effectifSituationHandicapSieService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifSituationHandicapSie> update(@PathVariable Integer id, @RequestBody EffectifSituationHandicapSie body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifSituationHandicapSieService::findById, effectifSituationHandicapSieService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifSituationHandicapSieService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
