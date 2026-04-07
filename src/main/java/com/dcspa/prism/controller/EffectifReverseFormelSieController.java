package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.entity.EffectifReverseFormelSie;
import com.dcspa.prism.service.EffectifReverseFormelSieService;
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
@RequestMapping("/api/effectif-reverse-formel-sie")
@RequiredArgsConstructor
public class EffectifReverseFormelSieController {

	private final EffectifReverseFormelSieService effectifReverseFormelSieService;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifReverseFormelSieService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifReverseFormelSieService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifReverseFormelSie body) {
		return ResponseEntity.status(201).body(toRow(effectifReverseFormelSieService.save(body)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifReverseFormelSie body) {
		Optional<EffectifReverseFormelSie> opt = effectifReverseFormelSieService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifReverseFormelSieService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifReverseFormelSieService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifReverseFormelSie e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		m.put("idNiveauSie", JpaAssociationIds.intIdOrNull(e.getIdNiveauSie()));
		m.put("idAnneeScolaire", JpaAssociationIds.intIdOrNull(e.getIdAnneeScolaire()));
		m.put("codeEffectifReverseFormelSie", e.getCodeEffectifReverseFormelSie());
		m.put("effectifReverseFormelSie3IvoirienH", e.getEffectifReverseFormelSie3IvoirienH());
		m.put("effectifReverseFormelSie3IvoirienF", e.getEffectifReverseFormelSie3IvoirienF());
		m.put("effectifReverseFormelSie3HandicapH", e.getEffectifReverseFormelSie3HandicapH());
		m.put("effectifReverseFormelSie3HandicapF", e.getEffectifReverseFormelSie3HandicapF());
		m.put("effectifReverseFormelSie3NonIvoirienF", e.getEffectifReverseFormelSie3NonIvoirienF());
		m.put("effectifReverseFormelSie3NonIvoirienH", e.getEffectifReverseFormelSie3NonIvoirienH());
		m.put("effectifReverseFormelSie46IvoirienF", e.getEffectifReverseFormelSie46IvoirienF());
		m.put("effectifReverseFormelSie46IvoirienH", e.getEffectifReverseFormelSie46IvoirienH());
		m.put("effectifReverseFormelSie46HandicapH", e.getEffectifReverseFormelSie46HandicapH());
		m.put("effectifReverseFormelSie46HandicapF", e.getEffectifReverseFormelSie46HandicapF());
		m.put("effectifReverseFormelSie46NonIvoiriienH", e.getEffectifReverseFormelSie46NonIvoiriienH());
		m.put("effectifReverseFormelSie46NonIvoiriienF", e.getEffectifReverseFormelSie46NonIvoiriienF());
		m.put("effectifReverseFormelSie79IvoirienH", e.getEffectifReverseFormelSie79IvoirienH());
		m.put("effectifReverseFormelSie79IvoirienF", e.getEffectifReverseFormelSie79IvoirienF());
		m.put("effectifReverseFormelSie79HandicapH", e.getEffectifReverseFormelSie79HandicapH());
		m.put("effectifReverseFormelSie79HandicapF", e.getEffectifReverseFormelSie79HandicapF());
		m.put("effectifReverseFormelSie79NonIvoirienF", e.getEffectifReverseFormelSie79NonIvoirienF());
		m.put("effectifReverseFormelSie79NonIvoirienH", e.getEffectifReverseFormelSie79NonIvoirienH());
		m.put("effectifReverseFormelSie1012IvoirienF", e.getEffectifReverseFormelSie1012IvoirienF());
		m.put("effectifReverseFormelSie1012IvoirienH", e.getEffectifReverseFormelSie1012IvoirienH());
		m.put("effectifReverseFormelSie1012HandicapH", e.getEffectifReverseFormelSie1012HandicapH());
		m.put("effectifReverseFormelSie1012HandicapF", e.getEffectifReverseFormelSie1012HandicapF());
		m.put("effectifReverseFormelSie1012NonIvoirienH", e.getEffectifReverseFormelSie1012NonIvoirienH());
		m.put("effectifReverseFormelSie1012NonIvoirienF", e.getEffectifReverseFormelSie1012NonIvoirienF());
		m.put("effectifReverseFormelSie1314EtPlusIvoirienF", e.getEffectifReverseFormelSie1314EtPlusIvoirienF());
		m.put("effectifReverseFormelSie1314EtPlusIvoirienH", e.getEffectifReverseFormelSie1314EtPlusIvoirienH());
		m.put("effectifReverseFormelSie1314EtPlusHandicapF", e.getEffectifReverseFormelSie1314EtPlusHandicapF());
		m.put("effectifReverseFormelSie1314EtPlusHandicapH", e.getEffectifReverseFormelSie1314EtPlusHandicapH());
		m.put("effectifReverseFormelSieNiveauSie", e.getEffectifReverseFormelSieNiveauSie());
		return m;
	}
}
