package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.EffectifCepeCec;
import com.dcspa.prism.service.EffectifCepeCecService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/api/effectif-cepe-cec")
@RequiredArgsConstructor
public class EffectifCepeCecController {

	private final EffectifCepeCecService effectifCepeCecService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifCepeCecService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifCepeCecService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifCepeCec body) {
		return ResponseEntity.status(201).body(toRow(effectifCepeCecService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifCepeCec body) {
		Optional<EffectifCepeCec> opt = effectifCepeCecService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifCepeCecService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifCepeCecService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifCepeCec e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "AnneeScolaire", e.getIdAnneeScolaire());
		ReferentielEnricher.putRef(m, "Centre", e.getIdCentre());
		m.put("cecIdCentre", JpaAssociationIds.intIdOrNull(e.getCecIdCentre()));
		m.put("codeEffectifCepeCec", e.getCodeEffectifCepeCec());
		m.put("effectifCepeCandidatFilleCec", e.getEffectifCepeCandidatFilleCec());
		m.put("effectifCepeCandidatGarconCec", e.getEffectifCepeCandidatGarconCec());
		m.put("effectifCepeCandidatIvoirienCec", e.getEffectifCepeCandidatIvoirienCec());
		m.put("effectifCepeCandidatHandicapFilleCec", e.getEffectifCepeCandidatHandicapFilleCec());
		m.put("effectifCepeCandidatHandicapGarconCec", e.getEffectifCepeCandidatHandicapGarconCec());
		m.put("effectifCepeAdmisFilleCec", e.getEffectifCepeAdmisFilleCec());
		m.put("effectifCepeAdmisGarconCec", e.getEffectifCepeAdmisGarconCec());
		m.put("effectifCepeAdmisIvoirienCec", e.getEffectifCepeAdmisIvoirienCec());
		m.put("effectifCepeAdmisHandicapFilleCec", e.getEffectifCepeAdmisHandicapFilleCec());
		m.put("effectifCepeAdmisHandicapGarconCec", e.getEffectifCepeAdmisHandicapGarconCec());
		return m;
	}
}
