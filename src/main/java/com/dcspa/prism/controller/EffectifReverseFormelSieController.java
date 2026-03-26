package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifReverseFormelSie;
import com.dcspa.prism.service.EffectifReverseFormelSieService;
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
@RequestMapping("/api/effectif-reverse-formel-sie")
@RequiredArgsConstructor
public class EffectifReverseFormelSieController {

	private final EffectifReverseFormelSieService effectifReverseFormelSieService;

	@GetMapping
	public ResponseEntity<List<EffectifReverseFormelSie>> findAll() {
		return ResponseEntity.ok(effectifReverseFormelSieService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifReverseFormelSie> findById(@PathVariable Integer id) {
		return effectifReverseFormelSieService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifReverseFormelSie> create(@RequestBody EffectifReverseFormelSie body) {
		return ResponseEntity.status(201).body(effectifReverseFormelSieService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifReverseFormelSie> update(@PathVariable Integer id, @RequestBody EffectifReverseFormelSie body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifReverseFormelSieService::findById, effectifReverseFormelSieService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifReverseFormelSieService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
