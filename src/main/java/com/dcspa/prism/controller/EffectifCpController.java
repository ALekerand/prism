package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.ReferentielEnricher;

import com.dcspa.prism.entity.EffectifCp;
import com.dcspa.prism.service.EffectifCpService;
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

@RestController
@RequestMapping("/api/effectif-cp")
@RequiredArgsConstructor
public class EffectifCpController {

	private final EffectifCpService effectifCpService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifCpService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifCpService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifCp body) {
		return ResponseEntity.status(201).body(toRow(effectifCpService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifCp body) {
		Optional<EffectifCp> opt = effectifCpService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifCpService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifCp entity) {
		Map<String, Object> row = new LinkedHashMap<>();
		row.put("id", entity.getId());
		ReferentielEnricher.putRef(row, "NiveauCp", entity.getIdNiveauCp());
		ReferentielEnricher.putRef(row, "AnneeScolaire", entity.getIdAnneeScolaire());
		ReferentielEnricher.putRef(row, "PeriodeActivite", entity.getIdPeriodeActivite());
		ReferentielEnricher.putRef(row, "Centre", entity.getIdCentre());
		row.put("codeEffectifCp", entity.getCodeEffectifCp());
		row.put("effectifCp911IvoirienH", entity.getEffectifCp911IvoirienH());
		row.put("effectifCp911IvoirienF", entity.getEffectifCp911IvoirienF());
		row.put("effectifCp911HandicapH", entity.getEffectifCp911HandicapH());
		row.put("effectifCp911HandicapF", entity.getEffectifCp911HandicapF());
		row.put("effectifCp911NonIvoirienF", entity.getEffectifCp911NonIvoirienF());
		row.put("effectifCp911NonIvoirienH", entity.getEffectifCp911NonIvoirienH());
		row.put("effectifCp1213IvoirienF", entity.getEffectifCp1213IvoirienF());
		row.put("effectifCp1213IvoirienH", entity.getEffectifCp1213IvoirienH());
		row.put("effectifCp1213HandicapH", entity.getEffectifCp1213HandicapH());
		row.put("effectifCp1213HandicapF", entity.getEffectifCp1213HandicapF());
		row.put("effectifCp1213NonIvoiriienH", entity.getEffectifCp1213NonIvoiriienH());
		row.put("effectifCp1213NonIvoiriienF", entity.getEffectifCp1213NonIvoiriienF());
		row.put("effectifCp14IvoirienH", entity.getEffectifCp14IvoirienH());
		row.put("effectifCp14IvoirienF", entity.getEffectifCp14IvoirienF());
		row.put("effectifCp14HandicapH", entity.getEffectifCp14HandicapH());
		row.put("effectifCp14HandicapF", entity.getEffectifCp14HandicapF());
		row.put("effectifCp14NonIvoirienF", entity.getEffectifCp14NonIvoirienF());
		row.put("effectifCp14NonIvoirienH", entity.getEffectifCp14NonIvoirienH());
		row.put("effectifCpNiveauCp", entity.getEffectifCpNiveauCp());
		row.put("effectifCpNiveauH", entity.getEffectifCpNiveauH());
		row.put("effectifCpNiveauF", entity.getEffectifCpNiveauF());
		return row;
	}
}
