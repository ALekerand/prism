package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifAbandonCp;
import com.dcspa.prism.service.EffectifAbandonCpService;
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
@RequestMapping("/api/effectif-abandon-cp")
@RequiredArgsConstructor
public class EffectifAbandonCpController {

	private final EffectifAbandonCpService effectifAbandonCpService;

	@GetMapping
	public ResponseEntity<List<EffectifAbandonCp>> findAll() {
		return ResponseEntity.ok(effectifAbandonCpService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifAbandonCp> findById(@PathVariable Integer id) {
		return effectifAbandonCpService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifAbandonCp> create(@RequestBody EffectifAbandonCp body) {
		return ResponseEntity.status(201).body(effectifAbandonCpService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifAbandonCp> update(@PathVariable Integer id, @RequestBody EffectifAbandonCp body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifAbandonCpService::findById, effectifAbandonCpService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifAbandonCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
