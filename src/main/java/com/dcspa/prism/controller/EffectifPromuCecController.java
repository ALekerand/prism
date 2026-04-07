package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.entity.EffectifPromuCec;
import com.dcspa.prism.service.EffectifPromuCecService;
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
@RequestMapping("/api/effectif-promu-cec")
@RequiredArgsConstructor
public class EffectifPromuCecController {

	private final EffectifPromuCecService effectifPromuCecService;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifPromuCecService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifPromuCecService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifPromuCec body) {
		return ResponseEntity.status(201).body(toRow(effectifPromuCecService.save(body)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifPromuCec body) {
		Optional<EffectifPromuCec> opt = effectifPromuCecService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifPromuCecService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifPromuCecService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifPromuCec e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		m.put("idNiveauSie", JpaAssociationIds.intIdOrNull(e.getIdNiveauSie()));
		m.put("idAnneeScolaire", JpaAssociationIds.intIdOrNull(e.getIdAnneeScolaire()));
		m.put("idCentre", JpaAssociationIds.intIdOrNull(e.getIdCentre()));
		m.put("codeEffectifPromuCec", e.getCodeEffectifPromuCec());
		m.put("effectifPromuCecMoins3F", e.getEffectifPromuCecMoins3F());
		m.put("effectifPromuCecMoins3H", e.getEffectifPromuCecMoins3H());
		m.put("effectifPromuCecMoins3IvoirienH", e.getEffectifPromuCecMoins3IvoirienH());
		m.put("effectifPromuCecMoins3IvoirienF", e.getEffectifPromuCecMoins3IvoirienF());
		m.put("effectifPromuCecMoins3HandicapH", e.getEffectifPromuCecMoins3HandicapH());
		m.put("effectifPromuCecMoins3HandicapF", e.getEffectifPromuCecMoins3HandicapF());
		m.put("effectifPromuCec35F", e.getEffectifPromuCec35F());
		m.put("effectifPromuCec35H", e.getEffectifPromuCec35H());
		m.put("effectifPromuCec35IvoirienH", e.getEffectifPromuCec35IvoirienH());
		m.put("effectifPromuCec35IvoirienF", e.getEffectifPromuCec35IvoirienF());
		m.put("effectifPromuCec35HandicapH", e.getEffectifPromuCec35HandicapH());
		m.put("effectifPromuCec35HandicapF", e.getEffectifPromuCec35HandicapF());
		m.put("effectifPromuCec68F", e.getEffectifPromuCec68F());
		m.put("effectifPromuCec68H", e.getEffectifPromuCec68H());
		m.put("effectifPromuCec68IvoirienF", e.getEffectifPromuCec68IvoirienF());
		m.put("effectifPromuCec68IvoirienH", e.getEffectifPromuCec68IvoirienH());
		m.put("effectifPromuCec68HandicapH", e.getEffectifPromuCec68HandicapH());
		m.put("effectifPromuCec68HandicapF", e.getEffectifPromuCec68HandicapF());
		m.put("effectifPromuCec911H", e.getEffectifPromuCec911H());
		m.put("effectifPromuCec911F", e.getEffectifPromuCec911F());
		m.put("effectifPromuCec911IvoirienH", e.getEffectifPromuCec911IvoirienH());
		m.put("effectifPromuCec911IvoirienF", e.getEffectifPromuCec911IvoirienF());
		m.put("effectifPromuCec911HandicapH", e.getEffectifPromuCec911HandicapH());
		m.put("effectifPromuCec911HandicapF", e.getEffectifPromuCec911HandicapF());
		m.put("effectifPromuCec1216F", e.getEffectifPromuCec1216F());
		m.put("effectifPromuCec1216H", e.getEffectifPromuCec1216H());
		m.put("effectifPromuCec1216IvoirienH", e.getEffectifPromuCec1216IvoirienH());
		m.put("effectifPromuCec1216IvoirienF", e.getEffectifPromuCec1216IvoirienF());
		m.put("effectifPromuCec1216HandicapH", e.getEffectifPromuCec1216HandicapH());
		m.put("effectifPromuCec1216HandicapF", e.getEffectifPromuCec1216HandicapF());
		m.put("effectifPromuCecNiveauCec", e.getEffectifPromuCecNiveauCec());
		return m;
	}
}
