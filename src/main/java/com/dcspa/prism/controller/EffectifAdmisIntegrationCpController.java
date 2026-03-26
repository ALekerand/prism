package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifAdmisIntegrationCp;
import com.dcspa.prism.service.EffectifAdmisIntegrationCpService;
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
@RequestMapping("/api/effectif-admis-integration-cp")
@RequiredArgsConstructor
public class EffectifAdmisIntegrationCpController {

	private final EffectifAdmisIntegrationCpService effectifAdmisIntegrationCpService;

	@GetMapping
	public ResponseEntity<List<EffectifAdmisIntegrationCp>> findAll() {
		return ResponseEntity.ok(effectifAdmisIntegrationCpService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifAdmisIntegrationCp> findById(@PathVariable Integer id) {
		return effectifAdmisIntegrationCpService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifAdmisIntegrationCp> create(@RequestBody EffectifAdmisIntegrationCp body) {
		return ResponseEntity.status(201).body(effectifAdmisIntegrationCpService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifAdmisIntegrationCp> update(@PathVariable Integer id, @RequestBody EffectifAdmisIntegrationCp body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifAdmisIntegrationCpService::findById, effectifAdmisIntegrationCpService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifAdmisIntegrationCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
