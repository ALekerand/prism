package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.entity.EffectifCepeCp;
import com.dcspa.prism.service.EffectifCepeCpService;
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
@RequestMapping("/api/effectif-cepe-cp")
@RequiredArgsConstructor
public class EffectifCepeCpController {

	private final EffectifCepeCpService effectifCepeCpService;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifCepeCpService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifCepeCpService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifCepeCp body) {
		return ResponseEntity.status(201).body(toRow(effectifCepeCpService.save(body)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifCepeCp body) {
		Optional<EffectifCepeCp> opt = effectifCepeCpService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifCepeCpService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifCepeCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifCepeCp e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		m.put("idAnneeScolaire", JpaAssociationIds.intIdOrNull(e.getIdAnneeScolaire()));
		m.put("idCentre", JpaAssociationIds.intIdOrNull(e.getIdCentre()));
		m.put("codeEffectifCepeCp", e.getCodeEffectifCepeCp());
		m.put("effectifCepeCandidatFCp", e.getEffectifCepeCandidatFCp());
		m.put("effectifCepeCandidatHCp", e.getEffectifCepeCandidatHCp());
		m.put("effectifCepeCandidatIvoirienCp", e.getEffectifCepeCandidatIvoirienCp());
		m.put("effectifCepeCandidatHandicapFCp", e.getEffectifCepeCandidatHandicapFCp());
		m.put("effectifCepeCandidatHandicapHCp", e.getEffectifCepeCandidatHandicapHCp());
		m.put("effectifCepeAdmisFCp", e.getEffectifCepeAdmisFCp());
		m.put("effectifCepeAdmisHCp", e.getEffectifCepeAdmisHCp());
		m.put("effectifCepeAdmisIvoirienCp", e.getEffectifCepeAdmisIvoirienCp());
		m.put("effectifCepeAdmisHandicapFCp", e.getEffectifCepeAdmisHandicapFCp());
		m.put("effectifCepeAdmisHandicapHCp", e.getEffectifCepeAdmisHandicapHCp());
		return m;
	}
}
