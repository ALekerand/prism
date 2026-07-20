package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.EffectifPromuSie;
import com.dcspa.prism.service.EffectifPromuSieService;
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
@RequestMapping("/api/effectif-promu-sie")
@RequiredArgsConstructor
public class EffectifPromuSieController {

	private final EffectifPromuSieService effectifPromuSieService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifPromuSieService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifPromuSieService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifPromuSie body) {
		return ResponseEntity.status(201).body(toRow(effectifPromuSieService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifPromuSie body) {
		Optional<EffectifPromuSie> opt = effectifPromuSieService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifPromuSieService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifPromuSieService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifPromuSie e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "AnneeScolaire", e.getIdAnneeScolaire());
		ReferentielEnricher.putRef(m, "PeriodeActivite", e.getIdPeriodeActivite());
		ReferentielEnricher.putRef(m, "NiveauSie", e.getIdNiveauSie());
		m.put("codeEffectifPromuSie", e.getCodeEffectifPromuSie());
		m.put("effectifPromuSie3IvoirienH", e.getEffectifPromuSie3IvoirienH());
		m.put("effectifPromuSie3IvoirienF", e.getEffectifPromuSie3IvoirienF());
		m.put("effectifPromuSie3HandicapH", e.getEffectifPromuSie3HandicapH());
		m.put("effectifPromuSie3HandicapF", e.getEffectifPromuSie3HandicapF());
		m.put("effectifPromuSie3NonIvoirienF", e.getEffectifPromuSie3NonIvoirienF());
		m.put("effectifPromuSie3NonIvoirienH", e.getEffectifPromuSie3NonIvoirienH());
		m.put("effectifPromuSie46IvoirienF", e.getEffectifPromuSie46IvoirienF());
		m.put("effectifPromuSie46IvoirienH", e.getEffectifPromuSie46IvoirienH());
		m.put("effectifPromuSie46HandicapH", e.getEffectifPromuSie46HandicapH());
		m.put("effectifPromuSie46HandicapF", e.getEffectifPromuSie46HandicapF());
		m.put("effectifPromuSie46NonIvoiriienH", e.getEffectifPromuSie46NonIvoiriienH());
		m.put("effectifPromuSie46NonIvoiriienF", e.getEffectifPromuSie46NonIvoiriienF());
		m.put("effectifPromuSie79IvoirienH", e.getEffectifPromuSie79IvoirienH());
		m.put("effectifPromuSie79IvoirienF", e.getEffectifPromuSie79IvoirienF());
		m.put("effectifPromuSie79HandicapH", e.getEffectifPromuSie79HandicapH());
		m.put("effectifPromuSie79HandicapF", e.getEffectifPromuSie79HandicapF());
		m.put("effectifPromuSie79NonIvoirienF", e.getEffectifPromuSie79NonIvoirienF());
		m.put("effectifPromuSie79NonIvoirienH", e.getEffectifPromuSie79NonIvoirienH());
		m.put("effectifPromuSie1012IvoirienF", e.getEffectifPromuSie1012IvoirienF());
		m.put("effectifPromuSie1012IvoirienH", e.getEffectifPromuSie1012IvoirienH());
		m.put("effectifPromuSie1012HandicapH", e.getEffectifPromuSie1012HandicapH());
		m.put("effectifPromuSie1012HandicapF", e.getEffectifPromuSie1012HandicapF());
		m.put("effectifPromuSie1012NonIvoirienH", e.getEffectifPromuSie1012NonIvoirienH());
		m.put("effectifPromuSie1012NonIvoirienF", e.getEffectifPromuSie1012NonIvoirienF());
		m.put("effectifPromuSie1314EtPlusIvoirienF", e.getEffectifPromuSie1314EtPlusIvoirienF());
		m.put("effectifPromuSie1314EtPlusIvoirienH", e.getEffectifPromuSie1314EtPlusIvoirienH());
		m.put("effectifPromuSie1314EtPlusHandicapF", e.getEffectifPromuSie1314EtPlusHandicapF());
		m.put("effectifPromuSie1314EtPlusHandicapH", e.getEffectifPromuSie1314EtPlusHandicapH());
		m.put("effectifPromuSieNiveauSie", e.getEffectifPromuSieNiveauSie());
		m.put("effectifPromuSieNiveauH", e.getEffectifPromuSieNiveauH());
		m.put("effectifPromuSieNiveauF", e.getEffectifPromuSieNiveauF());
		return m;
	}
}
