package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifSituationHandicapCp;
import com.dcspa.prism.service.EffectifSituationHandicapCpService;
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
@RequestMapping("/api/effectif-situation-handicap-cp")
@RequiredArgsConstructor
public class EffectifSituationHandicapCpController {

	private final EffectifSituationHandicapCpService effectifSituationHandicapCpService;

	@GetMapping
	public ResponseEntity<List<EffectifSituationHandicapCp>> findAll() {
		return ResponseEntity.ok(effectifSituationHandicapCpService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifSituationHandicapCp> findById(@PathVariable Integer id) {
		return effectifSituationHandicapCpService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifSituationHandicapCp> create(@RequestBody EffectifSituationHandicapCp body) {
		return ResponseEntity.status(201).body(effectifSituationHandicapCpService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifSituationHandicapCp> update(@PathVariable Integer id, @RequestBody EffectifSituationHandicapCp body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifSituationHandicapCpService::findById, effectifSituationHandicapCpService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifSituationHandicapCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
