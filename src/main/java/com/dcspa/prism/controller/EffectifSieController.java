package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.EffectifSie;
import com.dcspa.prism.service.EffectifSieService;
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
@RequestMapping("/api/effectif-sie")
@RequiredArgsConstructor
public class EffectifSieController {

	private final EffectifSieService effectifSieService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifSieService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifSieService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifSie body) {
		return ResponseEntity.status(201).body(toRow(effectifSieService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifSie body) {
		Optional<EffectifSie> opt = effectifSieService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifSieService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifSieService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifSie e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "AnneeScolaire", e.getIdAnneeScolaire());
		ReferentielEnricher.putRef(m, "PeriodeActivite", e.getIdPeriodeActivite());
		ReferentielEnricher.putRef(m, "NiveauSie", e.getIdNiveauSie());
		m.put("codeEffectifSie", e.getCodeEffectifSie());
		m.put("effectifSie3IvoirienH", e.getEffectifSie3IvoirienH());
		m.put("effectifSie3IvoirienF", e.getEffectifSie3IvoirienF());
		m.put("effectifSie3HandicapH", e.getEffectifSie3HandicapH());
		m.put("effectifSie3HandicapF", e.getEffectifSie3HandicapF());
		m.put("effectifSie3NonIvoirienF", e.getEffectifSie3NonIvoirienF());
		m.put("effectifSie3NonIvoirienH", e.getEffectifSie3NonIvoirienH());
		m.put("effectifSie46IvoirienF", e.getEffectifSie46IvoirienF());
		m.put("effectifSie46IvoirienH", e.getEffectifSie46IvoirienH());
		m.put("effectifSie46HandicapH", e.getEffectifSie46HandicapH());
		m.put("effectifSie46HandicapF", e.getEffectifSie46HandicapF());
		m.put("effectifSie46NonIvoiriienH", e.getEffectifSie46NonIvoiriienH());
		m.put("effectifSie79IvoirienH", e.getEffectifSie79IvoirienH());
		m.put("effectifSie79IvoirienF", e.getEffectifSie79IvoirienF());
		m.put("effectifSie79HandicapH", e.getEffectifSie79HandicapH());
		m.put("effectifSie79HandicapF", e.getEffectifSie79HandicapF());
		m.put("effectifSie79NonIvoirienF", e.getEffectifSie79NonIvoirienF());
		m.put("effectifSie79NonIvoirienH", e.getEffectifSie79NonIvoirienH());
		m.put("effectifSie1012IvoirienF", e.getEffectifSie1012IvoirienF());
		m.put("effectifSie1012IvoirienH", e.getEffectifSie1012IvoirienH());
		m.put("effectifSie1012HandicapH", e.getEffectifSie1012HandicapH());
		m.put("effectifSie1012HandicapF", e.getEffectifSie1012HandicapF());
		m.put("effectifSie1012NonIvoirienH", e.getEffectifSie1012NonIvoirienH());
		m.put("effectifSie1012NonIvoirienF", e.getEffectifSie1012NonIvoirienF());
		m.put("effectifSie1314EtPlusIvoirienF", e.getEffectifSie1314EtPlusIvoirienF());
		m.put("effectifSie1314EtPlusIvoirienH", e.getEffectifSie1314EtPlusIvoirienH());
		m.put("effectifSie1314EtPlusHandicapF", e.getEffectifSie1314EtPlusHandicapF());
		m.put("effectifSie1314EtPlusHandicapH", e.getEffectifSie1314EtPlusHandicapH());
		m.put("effectifSieNiveauSie", e.getEffectifSieNiveauSie());
		m.put("effectifSieNiveauH", e.getEffectifSieNiveauH());
		m.put("effectifSieNiveauF", e.getEffectifSieNiveauF());
		m.put("effectifSie46NonIvoiriienF", e.getEffectifSie46NonIvoiriienF());
		return m;
	}
}
