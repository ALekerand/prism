package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifIntegrationFormelCp;
import com.dcspa.prism.service.EffectifIntegrationFormelCpService;
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
@RequestMapping("/api/effectif-integration-formel-cp")
@RequiredArgsConstructor
public class EffectifIntegrationFormelCpController {

	private final EffectifIntegrationFormelCpService effectifIntegrationFormelCpService;

	@GetMapping
	public ResponseEntity<List<EffectifIntegrationFormelCp>> findAll() {
		return ResponseEntity.ok(effectifIntegrationFormelCpService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifIntegrationFormelCp> findById(@PathVariable Integer id) {
		return effectifIntegrationFormelCpService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifIntegrationFormelCp> create(@RequestBody EffectifIntegrationFormelCp body) {
		return ResponseEntity.status(201).body(effectifIntegrationFormelCpService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifIntegrationFormelCp> update(@PathVariable Integer id, @RequestBody EffectifIntegrationFormelCp body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifIntegrationFormelCpService::findById, effectifIntegrationFormelCpService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifIntegrationFormelCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
