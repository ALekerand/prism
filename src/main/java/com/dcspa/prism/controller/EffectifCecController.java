package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifCec;
import com.dcspa.prism.service.EffectifCecService;
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
@RequestMapping("/api/effectif-cec")
@RequiredArgsConstructor
public class EffectifCecController {

	private final EffectifCecService effectifCecService;

	@GetMapping
	public ResponseEntity<List<EffectifCec>> findAll() {
		return ResponseEntity.ok(effectifCecService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifCec> findById(@PathVariable Integer id) {
		return effectifCecService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifCec> create(@RequestBody EffectifCec body) {
		return ResponseEntity.status(201).body(effectifCecService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifCec> update(@PathVariable Integer id, @RequestBody EffectifCec body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifCecService::findById, effectifCecService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifCecService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
