package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.EffectifPassageAlpha;
import com.dcspa.prism.service.EffectifPassageAlphaService;
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
@RequestMapping("/api/effectif-passage-alpha")
@RequiredArgsConstructor
public class EffectifPassageAlphaController {

	private final EffectifPassageAlphaService effectifPassageAlphaService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(effectifPassageAlphaService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifPassageAlphaService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifPassageAlpha body) {
		EffectifPassageAlpha saved = effectifPassageAlphaService.save(body);
		return ResponseEntity.status(201).body(toRow(saved));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifPassageAlpha body) {
		Optional<EffectifPassageAlpha> opt = effectifPassageAlphaService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifPassageAlphaService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifPassageAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifPassageAlpha e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "PeriodeActivite", e.getIdPeriodeActivite());
		ReferentielEnricher.putRef(m, "Centre", e.getIdCentre());
		m.put("codeEffectifPassageAlpha", e.getCodeEffectifPassageAlpha());
		m.put("effectifPassageAlphaNiveauHomme", e.getEffectifPassageAlphaNiveauHomme());
		m.put("effectifPassageAlphaNiveauFemme", e.getEffectifPassageAlphaNiveauFemme());
		m.put("effectifPassageAlphaMoins15F", e.getEffectifPassageAlphaMoins15F());
		m.put("effectifPassageAlphaMoins15H", e.getEffectifPassageAlphaMoins15H());
		m.put("effectifPassageAlphaMoins15IvoirienH", e.getEffectifPassageAlphaMoins15IvoirienH());
		m.put("effectifPassageAlphaMoins15IvoirienF", e.getEffectifPassageAlphaMoins15IvoirienF());
		m.put("effectifPassageAlphaMoins15HandicapH", e.getEffectifPassageAlphaMoins15HandicapH());
		m.put("effectifPassageAlphaMoins15HandicapF", e.getEffectifPassageAlphaMoins15HandicapF());
		m.put("effectifPassageAlpha1524F", e.getEffectifPassageAlpha1524F());
		m.put("effectifPassageAlpha1524H", e.getEffectifPassageAlpha1524H());
		m.put("effectifPassageAlpha1524IvoirienH", e.getEffectifPassageAlpha1524IvoirienH());
		m.put("effectifPassageAlpha1524IvoirienF", e.getEffectifPassageAlpha1524IvoirienF());
		m.put("effectifPassageAlpha1524HandicapH", e.getEffectifPassageAlpha1524HandicapH());
		m.put("effectifPassageAlpha1524HandicapF", e.getEffectifPassageAlpha1524HandicapF());
		m.put("effectifPassageAlpha2549F", e.getEffectifPassageAlpha2549F());
		m.put("effectifPassageAlpha2549H", e.getEffectifPassageAlpha2549H());
		m.put("effectifPassageAlpha2549IvoirienF", e.getEffectifPassageAlpha2549IvoirienF());
		m.put("effectifPassageAlpha2549IvoirienH", e.getEffectifPassageAlpha2549IvoirienH());
		m.put("effectifPassageAlpha2549HandicapH", e.getEffectifPassageAlpha2549HandicapH());
		m.put("effectifPassageAlpha2549HandicapF", e.getEffectifPassageAlpha2549HandicapF());
		m.put("effectifPassageAlpha50PlusF", e.getEffectifPassageAlpha50PlusF());
		m.put("effectifPassageAlpha50PlusH", e.getEffectifPassageAlpha50PlusH());
		m.put("effectifPassageAlpha50PlusIvoirienH", e.getEffectifPassageAlpha50PlusIvoirienH());
		m.put("effectifApassageAlpha50PlusIvoirienF", e.getEffectifApassageAlpha50PlusIvoirienF());
		m.put("effectifPassageAlpha50PlusHandicapH", e.getEffectifPassageAlpha50PlusHandicapH());
		m.put("effectifPassageAlpha50PlusHandicapF", e.getEffectifPassageAlpha50PlusHandicapF());
		return m;
	}
}
