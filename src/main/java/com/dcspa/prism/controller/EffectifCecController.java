package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.ReferentielEnricher;

import com.dcspa.prism.entity.EffectifCec;
import com.dcspa.prism.service.EffectifCecService;
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
@RequestMapping("/api/effectif-cec")
@RequiredArgsConstructor
public class EffectifCecController {

	private final EffectifCecService effectifCecService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifCecService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifCecService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifCec body) {
		return ResponseEntity.status(201).body(toRow(effectifCecService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifCec body) {
		Optional<EffectifCec> opt = effectifCecService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifCecService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifCecService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifCec entity) {
		Map<String, Object> row = new LinkedHashMap<>();
		row.put("id", entity.getId());
		ReferentielEnricher.putRef(row, "PeriodeActivite", entity.getIdPeriodeActivite());
		ReferentielEnricher.putRef(row, "NiveauSie", entity.getIdNiveauSie());
		ReferentielEnricher.putRef(row, "Centre", entity.getIdCentre());
		row.put("codeEffectifCec", entity.getCodeEffectifCec());
		row.put("effectifCecMoins3F", entity.getEffectifCecMoins3F());
		row.put("effectifCecMoins3H", entity.getEffectifCecMoins3H());
		row.put("effectifCecMoins3IvoirienH", entity.getEffectifCecMoins3IvoirienH());
		row.put("effectifCecMoins3IvoirienF", entity.getEffectifCecMoins3IvoirienF());
		row.put("effectifCecMoins3HandicapH", entity.getEffectifCecMoins3HandicapH());
		row.put("effectifCecMoins3HandicapF", entity.getEffectifCecMoins3HandicapF());
		row.put("effectifCec35F", entity.getEffectifCec35F());
		row.put("effectifCec35H", entity.getEffectifCec35H());
		row.put("effectifCec35IvoirienH", entity.getEffectifCec35IvoirienH());
		row.put("effectifCec35IvoirienF", entity.getEffectifCec35IvoirienF());
		row.put("effectifCec35HandicapH", entity.getEffectifCec35HandicapH());
		row.put("effectifCec35HandicapF", entity.getEffectifCec35HandicapF());
		row.put("effectifCec68F", entity.getEffectifCec68F());
		row.put("effectifCec68H", entity.getEffectifCec68H());
		row.put("effectifCec68IvoirienF", entity.getEffectifCec68IvoirienF());
		row.put("effectifCec68IvoirienH", entity.getEffectifCec68IvoirienH());
		row.put("effectifCec68HandicapH", entity.getEffectifCec68HandicapH());
		row.put("effectifCec68HandicapF", entity.getEffectifCec68HandicapF());
		row.put("effectifCec911F", entity.getEffectifCec911F());
		row.put("effectifCec911H", entity.getEffectifCec911H());
		row.put("effectifCec911IvoirienH", entity.getEffectifCec911IvoirienH());
		row.put("effectifCec911IvoirienF", entity.getEffectifCec911IvoirienF());
		row.put("effectifCec911HandicapH", entity.getEffectifCec911HandicapH());
		row.put("effectifCec911HandicapF", entity.getEffectifCec911HandicapF());
		row.put("effectifCec1216F", entity.getEffectifCec1216F());
		row.put("effectifCec1216H", entity.getEffectifCec1216H());
		row.put("effectifCec1216IvoirienH", entity.getEffectifCec1216IvoirienH());
		row.put("effectifCec1216IvoirienF", entity.getEffectifCec1216IvoirienF());
		row.put("effectifCec1216HandicapH", entity.getEffectifCec1216HandicapH());
		row.put("effectifCec1216HandicapF", entity.getEffectifCec1216HandicapF());
		row.put("effectifCecNiveauCec", entity.getEffectifCecNiveauCec());
		row.put("effectifCecNiveauH", entity.getEffectifCecNiveauH());
		row.put("effectifCecNiveauF", entity.getEffectifCecNiveauF());
		return row;
	}
}
