package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.EffectifSituationHandicapSie;
import com.dcspa.prism.service.EffectifSituationHandicapSieService;
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
@RequestMapping("/api/effectif-situation-handicap-sie")
@RequiredArgsConstructor
public class EffectifSituationHandicapSieController {

	private final EffectifSituationHandicapSieService effectifSituationHandicapSieService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifSituationHandicapSieService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifSituationHandicapSieService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifSituationHandicapSie body) {
		EffectifSituationHandicapSie saved = effectifSituationHandicapSieService.save(body);
		return ResponseEntity.status(201).body(toRow(saved));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifSituationHandicapSie body) {
		Optional<EffectifSituationHandicapSie> opt = effectifSituationHandicapSieService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifSituationHandicapSieService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifSituationHandicapSieService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifSituationHandicapSie e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "NiveauSie", e.getIdNiveauSie());
		ReferentielEnricher.putRef(m, "AnneeScolaire", e.getIdAnneeScolaire());
		ReferentielEnricher.putRef(m, "PeriodeActivite", e.getIdPeriodeActivite());
		m.put("codeEffectifSituationHandicapSie", e.getCodeEffectifSituationHandicapSie());
		m.put("effectifSituationHandicapSie3IvoirienH", e.getEffectifSituationHandicapSie3IvoirienH());
		m.put("effectifSituationHandicapSie3IvoirienF", e.getEffectifSituationHandicapSie3IvoirienF());
		m.put("effectifSituationHandicapSie3NonIvoirienF", e.getEffectifSituationHandicapSie3NonIvoirienF());
		m.put("effectifSituationHandicapSie3NonIvoirienH", e.getEffectifSituationHandicapSie3NonIvoirienH());
		m.put("effectifSituationHandicapSie46IvoirienF", e.getEffectifSituationHandicapSie46IvoirienF());
		m.put("effectifSituationHandicapSie46IvoirienH", e.getEffectifSituationHandicapSie46IvoirienH());
		m.put("effectifSituationHandicapSie79IvoirienH", e.getEffectifSituationHandicapSie79IvoirienH());
		m.put("effectifSituationHandicapSie79IvoirienF", e.getEffectifSituationHandicapSie79IvoirienF());
		m.put("effectifSituationHandicapSie79NonIvoirienF", e.getEffectifSituationHandicapSie79NonIvoirienF());
		m.put("effectifSituationHandicapSie79NonIvoirienH", e.getEffectifSituationHandicapSie79NonIvoirienH());
		m.put("effectifSituationHandicapSie1012IvoirienF", e.getEffectifSituationHandicapSie1012IvoirienF());
		m.put("effectifSituationHandicapSie1012IvoirienH", e.getEffectifSituationHandicapSie1012IvoirienH());
		m.put("effectifSituationHandicaplSie1012NonIvoirienH", e.getEffectifSituationHandicaplSie1012NonIvoirienH());
		m.put("effectifSituationHandicapSie1012NonIvoirienF", e.getEffectifSituationHandicapSie1012NonIvoirienF());
		m.put("effectifSituationHandicapSie1314EtPlusIvoirienF", e.getEffectifSituationHandicapSie1314EtPlusIvoirienF());
		m.put("effectifSituationHandicapSie1314EtPlusIvoirienH", e.getEffectifSituationHandicapSie1314EtPlusIvoirienH());
		m.put("effectifSituationHandicapSieNiveauSie", e.getEffectifSituationHandicapSieNiveauSie());
		m.put("effectifSituationHandicapSieNiveauH", e.getEffectifSituationHandicapSieNiveauH());
		m.put("effectifSituationHandicapSieNiveauF", e.getEffectifSituationHandicapSieNiveauF());
		return m;
	}
}
