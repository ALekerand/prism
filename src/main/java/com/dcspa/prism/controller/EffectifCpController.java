package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifCp;
import com.dcspa.prism.service.EffectifCpService;
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
@RequestMapping("/api/effectif-cp")
@RequiredArgsConstructor
public class EffectifCpController {

	private final EffectifCpService effectifCpService;

	@GetMapping
	public ResponseEntity<List<EffectifCp>> findAll() {
		return ResponseEntity.ok(effectifCpService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifCp> findById(@PathVariable Integer id) {
		return effectifCpService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifCp> create(@RequestBody EffectifCp body) {
		return ResponseEntity.status(201).body(effectifCpService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifCp> update(@PathVariable Integer id, @RequestBody EffectifCp body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifCpService::findById, effectifCpService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
