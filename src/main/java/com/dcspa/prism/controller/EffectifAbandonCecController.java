package com.dcspa.prism.controller;

import com.dcspa.prism.entity.EffectifAbandonCec;
import com.dcspa.prism.service.EffectifAbandonCecService;
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
@RequestMapping("/api/effectif-abandon-cec")
@RequiredArgsConstructor
public class EffectifAbandonCecController {

	private final EffectifAbandonCecService effectifAbandonCecService;

	@GetMapping
	public ResponseEntity<List<EffectifAbandonCec>> findAll() {
		return ResponseEntity.ok(effectifAbandonCecService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifAbandonCec> findById(@PathVariable Integer id) {
		return effectifAbandonCecService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifAbandonCec> create(@RequestBody EffectifAbandonCec body) {
		return ResponseEntity.status(201).body(effectifAbandonCecService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifAbandonCec> update(@PathVariable Integer id, @RequestBody EffectifAbandonCec body) {
		body.setId(id);
		return ResponseEntity.ok(effectifAbandonCecService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifAbandonCecService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
