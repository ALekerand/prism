package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.EffectifAdmisIntegrationCp;
import com.dcspa.prism.service.EffectifAdmisIntegrationCpService;
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
@RequestMapping("/api/effectif-admis-integration-cp")
@RequiredArgsConstructor
public class EffectifAdmisIntegrationCpController {

	private final EffectifAdmisIntegrationCpService effectifAdmisIntegrationCpService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifAdmisIntegrationCpService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifAdmisIntegrationCpService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifAdmisIntegrationCp body) {
		return ResponseEntity.status(201).body(toRow(effectifAdmisIntegrationCpService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifAdmisIntegrationCp body) {
		Optional<EffectifAdmisIntegrationCp> opt = effectifAdmisIntegrationCpService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifAdmisIntegrationCpService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifAdmisIntegrationCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifAdmisIntegrationCp e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "NiveauCp", e.getIdNiveauCp());
		ReferentielEnricher.putRef(m, "AnneeScolaire", e.getIdAnneeScolaire());
		ReferentielEnricher.putRef(m, "PeriodeActivite", e.getIdPeriodeActivite());
		ReferentielEnricher.putRef(m, "Centre", e.getIdCentre());
		m.put("codeEffectifAdmisIntegrationCp", e.getCodeEffectifAdmisIntegrationCp());
		m.put("effectifAdmisIntegrationCp911IvoirienH", e.getEffectifAdmisIntegrationCp911IvoirienH());
		m.put("effectifAdmisIntegrationCp911IvoirienF", e.getEffectifAdmisIntegrationCp911IvoirienF());
		m.put("effectifAdmisIntegrationCp911HandicapH", e.getEffectifAdmisIntegrationCp911HandicapH());
		m.put("effectifAdmisIntegrationCp911HandicapF", e.getEffectifAdmisIntegrationCp911HandicapF());
		m.put("effectifAdmisIntegrationCp911NonIvoirienF", e.getEffectifAdmisIntegrationCp911NonIvoirienF());
		m.put("effectifAdmisIntegrationCp911NonIvoirienH", e.getEffectifAdmisIntegrationCp911NonIvoirienH());
		m.put("effectifAdmisIntegrationCp1213IvoirienF", e.getEffectifAdmisIntegrationCp1213IvoirienF());
		m.put("effectifAdmisIntegrationCp1213IvoirienH", e.getEffectifAdmisIntegrationCp1213IvoirienH());
		m.put("effectifAdmisIntegrationCp1213HandicapH", e.getEffectifAdmisIntegrationCp1213HandicapH());
		m.put("effectifAdmisIntegrationCp1213HandicapF", e.getEffectifAdmisIntegrationCp1213HandicapF());
		m.put("effectifAdmisIntegrationCp1213NonIvoiriienH", e.getEffectifAdmisIntegrationCp1213NonIvoiriienH());
		m.put("effectifAdmisIntegrationCp1213NonIvoiriienF", e.getEffectifAdmisIntegrationCp1213NonIvoiriienF());
		m.put("effectifAdmisIntegrationCp14IvoirienH", e.getEffectifAdmisIntegrationCp14IvoirienH());
		m.put("effectifAdmisIntegrationCp14IvoirienF", e.getEffectifAdmisIntegrationCp14IvoirienF());
		m.put("effectifAdmisIntegrationCp14HandicapH", e.getEffectifAdmisIntegrationCp14HandicapH());
		m.put("effectifAdmisIntegrationCp14HandicapF", e.getEffectifAdmisIntegrationCp14HandicapF());
		m.put("effectifAdmisIntegrationCp14NonIvoirienF", e.getEffectifAdmisIntegrationCp14NonIvoirienF());
		m.put("effectifAdmisIntegrationCp14NonIvoirienH", e.getEffectifAdmisIntegrationCp14NonIvoirienH());
		m.put("effectifAdmisIntegrationCpNiveauCp", e.getEffectifAdmisIntegrationCpNiveauCp());
		m.put("effectifAdmisIntegrationCpNiveauH", e.getEffectifAdmisIntegrationCpNiveauH());
		m.put("effectifAdmisIntegrationCpNiveauF", e.getEffectifAdmisIntegrationCpNiveauF());
		return m;
	}
}
