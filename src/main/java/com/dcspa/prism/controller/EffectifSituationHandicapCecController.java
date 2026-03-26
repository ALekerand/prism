package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.EffectifSituationHandicapCec;
import com.dcspa.prism.service.EffectifSituationHandicapCecService;
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
@RequestMapping("/api/effectif-situation-handicap-cec")
@RequiredArgsConstructor
public class EffectifSituationHandicapCecController {

	private final EffectifSituationHandicapCecService effectifSituationHandicapCecService;

	@GetMapping
	public ResponseEntity<List<EffectifSituationHandicapCec>> findAll() {
		return ResponseEntity.ok(effectifSituationHandicapCecService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EffectifSituationHandicapCec> findById(@PathVariable Integer id) {
		return effectifSituationHandicapCecService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EffectifSituationHandicapCec> create(@RequestBody EffectifSituationHandicapCec body) {
		return ResponseEntity.status(201).body(effectifSituationHandicapCecService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EffectifSituationHandicapCec> update(@PathVariable Integer id, @RequestBody EffectifSituationHandicapCec body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, effectifSituationHandicapCecService::findById, effectifSituationHandicapCecService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifSituationHandicapCecService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
