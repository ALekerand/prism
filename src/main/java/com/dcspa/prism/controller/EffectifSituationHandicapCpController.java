package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/effectif-situation-handicap-cp")
@RequiredArgsConstructor
public class EffectifSituationHandicapCpController {

	private final EffectifSituationHandicapCpService effectifSituationHandicapCpService;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifSituationHandicapCpService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifSituationHandicapCpService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifSituationHandicapCp body) {
		EffectifSituationHandicapCp saved = effectifSituationHandicapCpService.save(body);
		return ResponseEntity.status(201).body(toRow(saved));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifSituationHandicapCp body) {
		Optional<EffectifSituationHandicapCp> opt = effectifSituationHandicapCpService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifSituationHandicapCpService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifSituationHandicapCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifSituationHandicapCp e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		m.put("idAnneeScolaire", JpaAssociationIds.intIdOrNull(e.getIdAnneeScolaire()));
		m.put("idNiveauCp", JpaAssociationIds.intIdOrNull(e.getIdNiveauCp()));
		m.put("idCentre", JpaAssociationIds.intIdOrNull(e.getIdCentre()));
		m.put("codeEffectifSituationHandicapCp", e.getCodeEffectifSituationHandicapCp());
		m.put("effectifSituationHandicapCp911IvoirienH", e.getEffectifSituationHandicapCp911IvoirienH());
		m.put("effectifSituationHandicapCp911IvoirienF", e.getEffectifSituationHandicapCp911IvoirienF());
		m.put("effectifSituationHandicapCp911HandicapH", e.getEffectifSituationHandicapCp911HandicapH());
		m.put("effectifSituationHandicapCp911HandicapF", e.getEffectifSituationHandicapCp911HandicapF());
		m.put("effectifSituationHandicapCp911NonIvoirienF", e.getEffectifSituationHandicapCp911NonIvoirienF());
		m.put("effectifSituationHandicapCp911NonIvoirienH", e.getEffectifSituationHandicapCp911NonIvoirienH());
		m.put("effectifSituationHandicapCp1213IvoirienF", e.getEffectifSituationHandicapCp1213IvoirienF());
		m.put("effectifSituationHandicapCp1213IvoirienH", e.getEffectifSituationHandicapCp1213IvoirienH());
		m.put("effectifSituationHandicapCp1213HandicapH", e.getEffectifSituationHandicapCp1213HandicapH());
		m.put("effectifSituationHandicapCp1213HandicapF", e.getEffectifSituationHandicapCp1213HandicapF());
		m.put("effectifSituationHandicapCp1213NonIvoiriienH", e.getEffectifSituationHandicapCp1213NonIvoiriienH());
		m.put("effectifSituationHandicapCp1213NonIvoiriienF", e.getEffectifSituationHandicapCp1213NonIvoiriienF());
		m.put("effectifSituationHandicapCp14IvoirienH", e.getEffectifSituationHandicapCp14IvoirienH());
		m.put("effectifSituationHandicapCp14IvoirienF", e.getEffectifSituationHandicapCp14IvoirienF());
		m.put("effectifSituationHandicapCp14HandicapH", e.getEffectifSituationHandicapCp14HandicapH());
		m.put("effectifSituationHandicapCp14HandicapF", e.getEffectifSituationHandicapCp14HandicapF());
		m.put("effectifSituationHandicapCp14NonIvoirienF", e.getEffectifSituationHandicapCp14NonIvoirienF());
		m.put("effectifSituationHandicapCp14NonIvoirienH", e.getEffectifSituationHandicapCp14NonIvoirienH());
		m.put("effectifSituationHandicapCpNiveauCp", e.getEffectifSituationHandicapCpNiveauCp());
		return m;
	}
}
