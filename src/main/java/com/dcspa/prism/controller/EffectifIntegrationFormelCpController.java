package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.entity.EffectifIntegrationFormelCp;
import com.dcspa.prism.service.EffectifIntegrationFormelCpService;
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
@RequestMapping("/api/effectif-integration-formel-cp")
@RequiredArgsConstructor
public class EffectifIntegrationFormelCpController {

	private final EffectifIntegrationFormelCpService effectifIntegrationFormelCpService;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifIntegrationFormelCpService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifIntegrationFormelCpService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifIntegrationFormelCp body) {
		return ResponseEntity.status(201).body(toRow(effectifIntegrationFormelCpService.save(body)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifIntegrationFormelCp body) {
		Optional<EffectifIntegrationFormelCp> opt = effectifIntegrationFormelCpService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifIntegrationFormelCpService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifIntegrationFormelCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifIntegrationFormelCp e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		m.put("idNiveauCp", JpaAssociationIds.intIdOrNull(e.getIdNiveauCp()));
		m.put("idAnneeScolaire", JpaAssociationIds.intIdOrNull(e.getIdAnneeScolaire()));
		m.put("idCentre", JpaAssociationIds.intIdOrNull(e.getIdCentre()));
		m.put("codeEffectifIntegrationFormelCp", e.getCodeEffectifIntegrationFormelCp());
		m.put("effectifIntegrationFormelCp911IvoirienH", e.getEffectifIntegrationFormelCp911IvoirienH());
		m.put("effectifIntegrationFormelCp911IvoirienF", e.getEffectifIntegrationFormelCp911IvoirienF());
		m.put("effectifIntegrationFormelCp911HandicapH", e.getEffectifIntegrationFormelCp911HandicapH());
		m.put("effectifIntegrationFormelCp911HandicapF", e.getEffectifIntegrationFormelCp911HandicapF());
		m.put("effectifIntegrationFormelCp911NonIvoirienF", e.getEffectifIntegrationFormelCp911NonIvoirienF());
		m.put("effectifIntegrationFormelCp911NonIvoirienH", e.getEffectifIntegrationFormelCp911NonIvoirienH());
		m.put("effectifIntegrationFormelCp1213IvoirienF", e.getEffectifIntegrationFormelCp1213IvoirienF());
		m.put("effectifIntegrationFormelCp1213IvoirienH", e.getEffectifIntegrationFormelCp1213IvoirienH());
		m.put("effectifIntegrationFormelCp1213HandicapH", e.getEffectifIntegrationFormelCp1213HandicapH());
		m.put("effectifIntegrationFormelCp1213HandicapF", e.getEffectifIntegrationFormelCp1213HandicapF());
		m.put("effectifIntegrationFormelCp1213NonIvoiriienH", e.getEffectifIntegrationFormelCp1213NonIvoiriienH());
		m.put("effectifIntegrationFormelCp1213NonIvoiriienF", e.getEffectifIntegrationFormelCp1213NonIvoiriienF());
		m.put("effectifIntegrationFormelCp14IvoirienH", e.getEffectifIntegrationFormelCp14IvoirienH());
		m.put("effectifIntegrationFormelCp14IvoirienF", e.getEffectifIntegrationFormelCp14IvoirienF());
		m.put("effectifIntegrationFormelCp14HandicapH", e.getEffectifIntegrationFormelCp14HandicapH());
		m.put("effectifIntegrationFormelCp14HandicapF", e.getEffectifIntegrationFormelCp14HandicapF());
		m.put("effectifIntegrationFormelCp14NonIvoirienF", e.getEffectifIntegrationFormelCp14NonIvoirienF());
		m.put("effectifIntegrationFormelCp14NonIvoirienH", e.getEffectifIntegrationFormelCp14NonIvoirienH());
		m.put("effectifIntegrationFormelCpNiveauCp", e.getEffectifIntegrationFormelCpNiveauCp());
		return m;
	}
}
