package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.entity.EffectifSituationHandicapAlpha;
import com.dcspa.prism.service.EffectifSituationHandicapAlphaService;
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
@RequestMapping("/api/effectif-situation-handicap-alpha")
@RequiredArgsConstructor
public class EffectifSituationHandicapAlphaController {

	private final EffectifSituationHandicapAlphaService effectifSituationHandicapAlphaService;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifSituationHandicapAlphaService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifSituationHandicapAlphaService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifSituationHandicapAlpha body) {
		EffectifSituationHandicapAlpha saved = effectifSituationHandicapAlphaService.save(body);
		return ResponseEntity.status(201).body(toRow(saved));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifSituationHandicapAlpha body) {
		Optional<EffectifSituationHandicapAlpha> opt = effectifSituationHandicapAlphaService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifSituationHandicapAlphaService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifSituationHandicapAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifSituationHandicapAlpha e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		m.put("idPeriodeActivite", JpaAssociationIds.intIdOrNull(e.getIdPeriodeActivite()));
		m.put("idCentre", JpaAssociationIds.intIdOrNull(e.getIdCentre()));
		m.put("codeEffectifSituationHandicapAlpha", e.getCodeEffectifSituationHandicapAlpha());
		m.put("effectifSituationHandicapAlphaNiveauHomme", e.getEffectifSituationHandicapAlphaNiveauHomme());
		m.put("effectifSituationHandicapalphaNiveauFemme", e.getEffectifSituationHandicapalphaNiveauFemme());
		m.put("effectifSituationHandicapAlphaMoins15F", e.getEffectifSituationHandicapAlphaMoins15F());
		m.put("effectifSituationHandicapAlphaMoins15H", e.getEffectifSituationHandicapAlphaMoins15H());
		m.put("effectifSituationHandicapAlphaMoins15IvoirienH", e.getEffectifSituationHandicapAlphaMoins15IvoirienH());
		m.put("effectifSituationHandicapAlphaMoins15IvoirienF", e.getEffectifSituationHandicapAlphaMoins15IvoirienF());
		m.put("effectifSituationHandicapAlphaMoins15HandicapH", e.getEffectifSituationHandicapAlphaMoins15HandicapH());
		m.put("effectifSituationHandicapAlphaMoins15HandicapF", e.getEffectifSituationHandicapAlphaMoins15HandicapF());
		m.put("effectifSituationHandicapAlpha1524F", e.getEffectifSituationHandicapAlpha1524F());
		m.put("effectifSituationHandicapAlpha1524H", e.getEffectifSituationHandicapAlpha1524H());
		m.put("effectifSituationHandicapAlpha1524IvoirienH", e.getEffectifSituationHandicapAlpha1524IvoirienH());
		m.put("effectifSituationHandicapAlpha1524IvoirienF", e.getEffectifSituationHandicapAlpha1524IvoirienF());
		m.put("effectifSituationHandicapAlpha1524HandicapH", e.getEffectifSituationHandicapAlpha1524HandicapH());
		m.put("effectifSituationHandicapAlpha1524HandicapF", e.getEffectifSituationHandicapAlpha1524HandicapF());
		m.put("effectifSituationHandicapAlpha2549F", e.getEffectifSituationHandicapAlpha2549F());
		m.put("effectifSituationHandicapAlpha2549H", e.getEffectifSituationHandicapAlpha2549H());
		m.put("effectifSituationHandicapAlpha2549IvoirienF", e.getEffectifSituationHandicapAlpha2549IvoirienF());
		m.put("effectifSituationHandicapAlpha2549IvoirienH", e.getEffectifSituationHandicapAlpha2549IvoirienH());
		m.put("effectifSituationHandicapAlpha2549HandicapH", e.getEffectifSituationHandicapAlpha2549HandicapH());
		m.put("effectifSituationHandicapAlpha2549HandicapF", e.getEffectifSituationHandicapAlpha2549HandicapF());
		m.put("effectifSituationHandicapAlpha50PlusF", e.getEffectifSituationHandicapAlpha50PlusF());
		m.put("effectifSituationHandicapAlpha50PlusH", e.getEffectifSituationHandicapAlpha50PlusH());
		m.put("effectifSituationHandicapAlpha50PlusIvoirienH", e.getEffectifSituationHandicapAlpha50PlusIvoirienH());
		m.put("effectifSituationHandicapAlpha50PlusIvoirienF", e.getEffectifSituationHandicapAlpha50PlusIvoirienF());
		m.put("effectifSituationHandicapAlpha50PlusHandicapH", e.getEffectifSituationHandicapAlpha50PlusHandicapH());
		m.put("effectifSituationHandicapAlpha50PlusHandicapF", e.getEffectifSituationHandicapAlpha50PlusHandicapF());
		return m;
	}
}
