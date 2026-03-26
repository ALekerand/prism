package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifPromuCec;
import com.dcspa.prism.service.EffectifPromuCecService;
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
@RequestMapping("/api/effectif-promu-cec")
@RequiredArgsConstructor
public class EffectifPromuCecController {

	private final EffectifPromuCecService effectifPromuCecService;

	@GetMapping
	public ResponseEntity<List<EffectifPromuCec>> findAll() {
		return ResponseEntity.ok(effectifPromuCecService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifPromuCec> findById(@PathVariable Integer id) {
		return effectifPromuCecService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifPromuCec> create(@RequestBody EffectifPromuCec body) {
		return ResponseEntity.status(201).body(effectifPromuCecService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifPromuCec> update(@PathVariable Integer id, @RequestBody EffectifPromuCec body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifPromuCecService::findById, effectifPromuCecService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifPromuCecService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
