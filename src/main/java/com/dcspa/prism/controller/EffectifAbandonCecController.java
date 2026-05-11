package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.EffectifAbandonCec;
import com.dcspa.prism.service.EffectifAbandonCecService;
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
@RequestMapping("/api/effectif-abandon-cec")
@RequiredArgsConstructor
public class EffectifAbandonCecController {

	private final EffectifAbandonCecService effectifAbandonCecService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifAbandonCecService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifAbandonCecService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifAbandonCec body) {
		EffectifAbandonCec saved = effectifAbandonCecService.save(body);
		return ResponseEntity.status(201).body(toRow(saved));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifAbandonCec body) {
		Optional<EffectifAbandonCec> opt = effectifAbandonCecService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifAbandonCecService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifAbandonCecService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifAbandonCec e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "AnneeScolaire", e.getIdAnneeScolaire());
		ReferentielEnricher.putRef(m, "NiveauSie", e.getIdNiveauSie());
		ReferentielEnricher.putRef(m, "Centre", e.getIdCentre());
		m.put("codeEffectifAbandonCec", e.getCodeEffectifAbandonCec());
		m.put("effectifAbandonCecMoins3F", e.getEffectifAbandonCecMoins3F());
		m.put("effectifAbandonCecMoins3H", e.getEffectifAbandonCecMoins3H());
		m.put("effectifAbandonCecMoins3IvoirienH", e.getEffectifAbandonCecMoins3IvoirienH());
		m.put("effectifAbandonCecMoins3IvoirienF", e.getEffectifAbandonCecMoins3IvoirienF());
		m.put("effectifAbandonCecMoins3HandicapH", e.getEffectifAbandonCecMoins3HandicapH());
		m.put("effectifAbandonCecMoins3HandicapF", e.getEffectifAbandonCecMoins3HandicapF());
		m.put("effectifAbandonCec35F", e.getEffectifAbandonCec35F());
		m.put("effectifAbandonCec35H", e.getEffectifAbandonCec35H());
		m.put("effectifAbandonCec35IvoirienH", e.getEffectifAbandonCec35IvoirienH());
		m.put("effectifAbandonCec35IvoirienF", e.getEffectifAbandonCec35IvoirienF());
		m.put("effectifAbandonCec35HandicapH", e.getEffectifAbandonCec35HandicapH());
		m.put("effectifAbandonCec35HandicapF", e.getEffectifAbandonCec35HandicapF());
		m.put("effectifAbandonCec68F", e.getEffectifAbandonCec68F());
		m.put("effectifAbandonCec68H", e.getEffectifAbandonCec68H());
		m.put("effectifAbandonCec68IvoirienF", e.getEffectifAbandonCec68IvoirienF());
		m.put("effectifAbandonCec68IvoirienH", e.getEffectifAbandonCec68IvoirienH());
		m.put("effectifAbandonCec68HandicapH", e.getEffectifAbandonCec68HandicapH());
		m.put("effectifAbandonCec68HandicapF", e.getEffectifAbandonCec68HandicapF());
		m.put("effectifAbandonCec911H", e.getEffectifAbandonCec911H());
		m.put("effectifAbandonCec911F", e.getEffectifAbandonCec911F());
		m.put("effectifAbandonCec911IvoirienH", e.getEffectifAbandonCec911IvoirienH());
		m.put("effectifAbandonCec911IvoirienF", e.getEffectifAbandonCec911IvoirienF());
		m.put("effectifAbandonCec911HandicapH", e.getEffectifAbandonCec911HandicapH());
		m.put("effectifAbandonCec911HandicapF", e.getEffectifAbandonCec911HandicapF());
		m.put("effectifAbandonCec1216F", e.getEffectifAbandonCec1216F());
		m.put("effectifAbandonCec1216H", e.getEffectifAbandonCec1216H());
		m.put("effectifAbandonCec1216IvoirienH", e.getEffectifAbandonCec1216IvoirienH());
		m.put("effectifAbandonCec1216IvoirienF", e.getEffectifAbandonCec1216IvoirienF());
		m.put("effectifAbandonCec1216HandicapH", e.getEffectifAbandonCec1216HandicapH());
		m.put("effectifAbandonCec1216HandicapF", e.getEffectifAbandonCec1216HandicapF());
		m.put("effectifAbandonCecNiveauCec", e.getEffectifAbandonCecNiveauCec());
		m.put("causeAbandonCec", e.getCauseAbandonCec());
		return m;
	}
}
