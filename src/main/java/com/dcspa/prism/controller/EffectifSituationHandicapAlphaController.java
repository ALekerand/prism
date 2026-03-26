package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifSituationHandicapAlpha;
import com.dcspa.prism.service.EffectifSituationHandicapAlphaService;
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
@RequestMapping("/api/effectif-situation-handicap-alpha")
@RequiredArgsConstructor
public class EffectifSituationHandicapAlphaController {

	private final EffectifSituationHandicapAlphaService effectifSituationHandicapAlphaService;

	@GetMapping
	public ResponseEntity<List<EffectifSituationHandicapAlpha>> findAll() {
		return ResponseEntity.ok(effectifSituationHandicapAlphaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifSituationHandicapAlpha> findById(@PathVariable Integer id) {
		return effectifSituationHandicapAlphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifSituationHandicapAlpha> create(@RequestBody EffectifSituationHandicapAlpha body) {
		return ResponseEntity.status(201).body(effectifSituationHandicapAlphaService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifSituationHandicapAlpha> update(@PathVariable Integer id, @RequestBody EffectifSituationHandicapAlpha body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifSituationHandicapAlphaService::findById, effectifSituationHandicapAlphaService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifSituationHandicapAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
