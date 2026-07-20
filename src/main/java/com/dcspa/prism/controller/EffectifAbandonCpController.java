package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.EffectifAbandonCp;
import com.dcspa.prism.service.EffectifAbandonCpService;
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
@RequestMapping("/api/effectif-abandon-cp")
@RequiredArgsConstructor
public class EffectifAbandonCpController {

	private final EffectifAbandonCpService effectifAbandonCpService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifAbandonCpService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifAbandonCpService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifAbandonCp body) {
		EffectifAbandonCp saved = effectifAbandonCpService.save(body);
		return ResponseEntity.status(201).body(toRow(saved));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifAbandonCp body) {
		Optional<EffectifAbandonCp> opt = effectifAbandonCpService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifAbandonCpService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifAbandonCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifAbandonCp e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "NiveauCp", e.getIdNiveauCp());
		ReferentielEnricher.putRef(m, "AnneeScolaire", e.getIdAnneeScolaire());
		ReferentielEnricher.putRef(m, "PeriodeActivite", e.getIdPeriodeActivite());
		ReferentielEnricher.putRef(m, "Centre", e.getIdCentre());
		m.put("codeEffectifAbandonCp", e.getCodeEffectifAbandonCp());
		m.put("effectifAbandonCp911IvoirienH", e.getEffectifAbandonCp911IvoirienH());
		m.put("effectifAbandonCp911IvoirienF", e.getEffectifAbandonCp911IvoirienF());
		m.put("effectifAbandonCp911HandicapH", e.getEffectifAbandonCp911HandicapH());
		m.put("effectifAbandonCp911HandicapF", e.getEffectifAbandonCp911HandicapF());
		m.put("effectifAbandonCp911NonIvoirienF", e.getEffectifAbandonCp911NonIvoirienF());
		m.put("effectifAbandonCp911NonIvoirienH", e.getEffectifAbandonCp911NonIvoirienH());
		m.put("effectifAbandonCp1213IvoirienF", e.getEffectifAbandonCp1213IvoirienF());
		m.put("effectifAbandonCp1213IvoirienH", e.getEffectifAbandonCp1213IvoirienH());
		m.put("effectifAbandonCp1213HandicapH", e.getEffectifAbandonCp1213HandicapH());
		m.put("effectifAbandonCp1213HandicapF", e.getEffectifAbandonCp1213HandicapF());
		m.put("effectifAbandonCp1213NonIvoiriienH", e.getEffectifAbandonCp1213NonIvoiriienH());
		m.put("effectifAbandonCp14IvoirienH", e.getEffectifAbandonCp14IvoirienH());
		m.put("effectifAbandonCp14IvoirienF", e.getEffectifAbandonCp14IvoirienF());
		m.put("effectifAbandonCp14HandicapH", e.getEffectifAbandonCp14HandicapH());
		m.put("effectifAbandonCp14HandicapF", e.getEffectifAbandonCp14HandicapF());
		m.put("effectifAbandonCp14NonIvoirienF", e.getEffectifAbandonCp14NonIvoirienF());
		m.put("effectifAbandonCp14NonIvoirienH", e.getEffectifAbandonCp14NonIvoirienH());
		m.put("effectifAbandonCpNiveauCp", e.getEffectifAbandonCpNiveauCp());
		m.put("effectifAbandonCpNiveauH", e.getEffectifAbandonCpNiveauH());
		m.put("effectifAbandonCpNiveauF", e.getEffectifAbandonCpNiveauF());
		m.put("causeAbandonCp", e.getCauseAbandonCp());
		return m;
	}
}
