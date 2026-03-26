package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifCepeCec;
import com.dcspa.prism.service.EffectifCepeCecService;
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
@RequestMapping("/api/effectif-cepe-cec")
@RequiredArgsConstructor
public class EffectifCepeCecController {

	private final EffectifCepeCecService effectifCepeCecService;

	@GetMapping
	public ResponseEntity<List<EffectifCepeCec>> findAll() {
		return ResponseEntity.ok(effectifCepeCecService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifCepeCec> findById(@PathVariable Integer id) {
		return effectifCepeCecService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifCepeCec> create(@RequestBody EffectifCepeCec body) {
		return ResponseEntity.status(201).body(effectifCepeCecService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifCepeCec> update(@PathVariable Integer id, @RequestBody EffectifCepeCec body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifCepeCecService::findById, effectifCepeCecService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifCepeCecService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
