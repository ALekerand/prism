package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.entity.EffectifAbondanSie;
import com.dcspa.prism.service.EffectifAbondanSieService;
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
@RequestMapping("/api/effectif-abondan-sie")
@RequiredArgsConstructor
public class EffectifAbondanSieController {

	private final EffectifAbondanSieService effectifAbondanSieService;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifAbondanSieService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifAbondanSieService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifAbondanSie body) {
		EffectifAbondanSie saved = effectifAbondanSieService.save(body);
		return ResponseEntity.status(201).body(toRow(saved));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifAbondanSie body) {
		Optional<EffectifAbondanSie> opt = effectifAbondanSieService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifAbondanSieService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifAbondanSieService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifAbondanSie e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		m.put("idAnneeScolaire", JpaAssociationIds.intIdOrNull(e.getIdAnneeScolaire()));
		m.put("idNiveauSie", JpaAssociationIds.intIdOrNull(e.getIdNiveauSie()));
		m.put("codeAbandonEffectifSie", e.getCodeAbandonEffectifSie());
		m.put("effectifAbandonSie3IvoirienH", e.getEffectifAbandonSie3IvoirienH());
		m.put("effectifAbandonSie3IvoirienF", e.getEffectifAbandonSie3IvoirienF());
		m.put("effectifAbandonSie3HandicapH", e.getEffectifAbandonSie3HandicapH());
		m.put("effectifAbandonSie3HandicapF", e.getEffectifAbandonSie3HandicapF());
		m.put("effectifAbandonSie3NonIvoirienF", e.getEffectifAbandonSie3NonIvoirienF());
		m.put("effectifAbandonSie3NonIvoirienH", e.getEffectifAbandonSie3NonIvoirienH());
		m.put("effectifAbandonSie46IvoirienF", e.getEffectifAbandonSie46IvoirienF());
		m.put("effectifAbandonSie46IvoirienH", e.getEffectifAbandonSie46IvoirienH());
		m.put("effectifAbandonSie46HandicapH", e.getEffectifAbandonSie46HandicapH());
		m.put("effectifAbandonSie46HandicapF", e.getEffectifAbandonSie46HandicapF());
		m.put("effectifAbandonSie46NonIvoiriienH", e.getEffectifAbandonSie46NonIvoiriienH());
		m.put("effectifAbandonSie46NonIvoiriienF", e.getEffectifAbandonSie46NonIvoiriienF());
		m.put("effectifAbandonSie79IvoirienH", e.getEffectifAbandonSie79IvoirienH());
		m.put("effectifAbandonSie79IvoirienF", e.getEffectifAbandonSie79IvoirienF());
		m.put("effectifAbandonSie79HandicapH", e.getEffectifAbandonSie79HandicapH());
		m.put("effectifAbandonSie79HandicapF", e.getEffectifAbandonSie79HandicapF());
		m.put("effectifAbandonSie79NonIvoirienF", e.getEffectifAbandonSie79NonIvoirienF());
		m.put("effectifAbandonSie79NonIvoirienH", e.getEffectifAbandonSie79NonIvoirienH());
		m.put("effectifAbandonSie1012IvoirienF", e.getEffectifAbandonSie1012IvoirienF());
		m.put("effectifAbandonSie1012IvoirienH", e.getEffectifAbandonSie1012IvoirienH());
		m.put("effectifAbandonSie1012HandicapH", e.getEffectifAbandonSie1012HandicapH());
		m.put("effectifAbandonSie1012HandicapF", e.getEffectifAbandonSie1012HandicapF());
		m.put("effectifAbandonSie1012NonIvoirienH", e.getEffectifAbandonSie1012NonIvoirienH());
		m.put("effectifAbandonSie1012NonIvoirienF", e.getEffectifAbandonSie1012NonIvoirienF());
		m.put("effectifAbandonSie1314EtPlusIvoirienF", e.getEffectifAbandonSie1314EtPlusIvoirienF());
		m.put("effectifAbandonSie1314EtPlusIvoirienH", e.getEffectifAbandonSie1314EtPlusIvoirienH());
		m.put("effectifAbandonSie1314EtPlusHandicapF", e.getEffectifAbandonSie1314EtPlusHandicapF());
		m.put("effectifAbandonSie1314EtPlusHandicapH", e.getEffectifAbandonSie1314EtPlusHandicapH());
		m.put("effectifAbandonSie1314EtPlusNonIvoirienF", e.getEffectifAbandonSie1314EtPlusNonIvoirienF());
		m.put("effectifAbandonSie1314EtPlusNonIvoirienH", e.getEffectifAbandonSie1314EtPlusNonIvoirienH());
		m.put("effectifAbandonSieNiveauSie", e.getEffectifAbandonSieNiveauSie());
		m.put("causeAbandonSie", e.getCauseAbandonSie());
		return m;
	}
}
