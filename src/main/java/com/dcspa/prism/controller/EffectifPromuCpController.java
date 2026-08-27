package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.EffectifPromuCp;
import com.dcspa.prism.service.EffectifPromuCpService;
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
@RequestMapping("/api/effectif-promu-cp")
@RequiredArgsConstructor
public class EffectifPromuCpController {

	private final EffectifPromuCpService effectifPromuCpService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifPromuCpService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifPromuCpService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifPromuCp body) {
		return ResponseEntity.status(201).body(toRow(effectifPromuCpService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifPromuCp body) {
		Optional<EffectifPromuCp> opt = effectifPromuCpService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifPromuCpService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifPromuCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifPromuCp e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "NiveauCp", e.getIdNiveauCp());
		ReferentielEnricher.putRef(m, "AnneeScolaire", e.getIdAnneeScolaire());
		ReferentielEnricher.putRef(m, "PeriodeActivite", e.getIdPeriodeActivite());
		ReferentielEnricher.putRef(m, "Centre", e.getIdCentre());
		m.put("codeEffectifPromuCp", e.getCodeEffectifPromuCp());
		m.put("effectifPromuCp911IvoirienH", e.getEffectifPromuCp911IvoirienH());
		m.put("effectifPromuCp911IvoirienF", e.getEffectifPromuCp911IvoirienF());
		m.put("effectifPromuCp911HandicapH", e.getEffectifPromuCp911HandicapH());
		m.put("effectifPromuCp911HandicapF", e.getEffectifPromuCp911HandicapF());
		m.put("effectifPromuCp911NonIvoirienF", e.getEffectifPromuCp911NonIvoirienF());
		m.put("effectifPromuCp911NonIvoirienH", e.getEffectifPromuCp911NonIvoirienH());
		m.put("effectifPromuCp1213IvoirienF", e.getEffectifPromuCp1213IvoirienF());
		m.put("effectifPromuCp1213IvoirienH", e.getEffectifPromuCp1213IvoirienH());
		m.put("effectifPromuCp1213HandicapH", e.getEffectifPromuCp1213HandicapH());
		m.put("effectifPromuCp1213HandicapF", e.getEffectifPromuCp1213HandicapF());
		m.put("effectifPromuCp1213NonIvoiriienH", e.getEffectifPromuCp1213NonIvoiriienH());
		m.put("effectifPromuCp1213NonIvoiriienF", e.getEffectifPromuCp1213NonIvoiriienF());
		m.put("effectifPromuCp14IvoirienH", e.getEffectifPromuCp14IvoirienH());
		m.put("effectifPromuCp14IvoirienF", e.getEffectifPromuCp14IvoirienF());
		m.put("effectifPromuCp14HandicapH", e.getEffectifPromuCp14HandicapH());
		m.put("effectifPromuCp14HandicapF", e.getEffectifPromuCp14HandicapF());
		m.put("effectifPromuCp14NonIvoirienF", e.getEffectifPromuCp14NonIvoirienF());
		m.put("effectifPromuCp14NonIvoirienH", e.getEffectifPromuCp14NonIvoirienH());
		m.put("effectifPromuCpNiveauCp", e.getEffectifPromuCpNiveauCp());
		m.put("effectifPromuCpNiveauH", e.getEffectifPromuCpNiveauH());
		m.put("effectifPromuCpNiveauF", e.getEffectifPromuCpNiveauF());
		return m;
	}
}
