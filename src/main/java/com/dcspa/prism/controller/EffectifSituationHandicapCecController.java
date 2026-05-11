package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.EffectifSituationHandicapCec;
import com.dcspa.prism.service.EffectifSituationHandicapCecService;
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
@RequestMapping("/api/effectif-situation-handicap-cec")
@RequiredArgsConstructor
public class EffectifSituationHandicapCecController {

	private final EffectifSituationHandicapCecService effectifSituationHandicapCecService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifSituationHandicapCecService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifSituationHandicapCecService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifSituationHandicapCec body) {
		EffectifSituationHandicapCec saved = effectifSituationHandicapCecService.save(body);
		return ResponseEntity.status(201).body(toRow(saved));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifSituationHandicapCec body) {
		Optional<EffectifSituationHandicapCec> opt = effectifSituationHandicapCecService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifSituationHandicapCecService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifSituationHandicapCecService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifSituationHandicapCec e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "NiveauSie", e.getIdNiveauSie());
		ReferentielEnricher.putRef(m, "AnneeScolaire", e.getIdAnneeScolaire());
		m.put("codeEffectifSituationHandicapCec", e.getCodeEffectifSituationHandicapCec());
		m.put("effectifSituationHandicapCecMoins3F", e.getEffectifSituationHandicapCecMoins3F());
		m.put("effectifSituationHandicapCecMoins3H", e.getEffectifSituationHandicapCecMoins3H());
		m.put("effectifSituationHandicapCecMoins3IvoirienH", e.getEffectifSituationHandicapCecMoins3IvoirienH());
		m.put("effectifSituationHandicapCecMoins3IvoirienF", e.getEffectifSituationHandicapCecMoins3IvoirienF());
		m.put("effectifSituationHandicapCec35F", e.getEffectifSituationHandicapCec35F());
		m.put("effectifSituationHandicapCec35H", e.getEffectifSituationHandicapCec35H());
		m.put("effectifSituationHandicapCec35IvoirienH", e.getEffectifSituationHandicapCec35IvoirienH());
		m.put("effectifSituationHandicapCec35IvoirienF", e.getEffectifSituationHandicapCec35IvoirienF());
		m.put("effectifSituationHandicapCec68F", e.getEffectifSituationHandicapCec68F());
		m.put("effectifSituationHandicapCec68H", e.getEffectifSituationHandicapCec68H());
		m.put("effectifSituationHandicapCec68IvoirienF", e.getEffectifSituationHandicapCec68IvoirienF());
		m.put("effectifSituationHandicapCec68IvoirienH", e.getEffectifSituationHandicapCec68IvoirienH());
		m.put("effectifSituationHandicapCec911IvoirienH", e.getEffectifSituationHandicapCec911IvoirienH());
		m.put("effectifSituationHandicapCec911IvoirienF", e.getEffectifSituationHandicapCec911IvoirienF());
		m.put("effectifSituationHandicapCec1216F", e.getEffectifSituationHandicapCec1216F());
		m.put("effectifSituationHandicapCec1216H", e.getEffectifSituationHandicapCec1216H());
		m.put("effectifSituationHandicapCec1216IvoirienH", e.getEffectifSituationHandicapCec1216IvoirienH());
		m.put("effectifSituationHandicapCec1216IvoirienF", e.getEffectifSituationHandicapCec1216IvoirienF());
		m.put("effectifSituationHandicapCecNiveauCec", e.getEffectifSituationHandicapCecNiveauCec());
		return m;
	}
}
