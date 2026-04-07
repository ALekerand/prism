package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.entity.EffectifAbandonAlpha;
import com.dcspa.prism.service.EffectifAbandonAlphaService;
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
@RequestMapping("/api/effectif-abandon-alpha")
@RequiredArgsConstructor
public class EffectifAbandonAlphaController {

	private final EffectifAbandonAlphaService effectifAbandonAlphaService;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		List<Map<String, Object>> rows = effectifAbandonAlphaService.findAll().stream()
				.map(this::toRow)
				.collect(Collectors.toList());
		return ResponseEntity.ok(rows);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifAbandonAlphaService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody EffectifAbandonAlpha body) {
		EffectifAbandonAlpha saved = effectifAbandonAlphaService.save(body);
		return ResponseEntity.status(201).body(toRow(saved));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifAbandonAlpha body) {
		Optional<EffectifAbandonAlpha> opt = effectifAbandonAlphaService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		EffectifAbandonAlpha saved = effectifAbandonAlphaService.save(body);
		return ResponseEntity.ok(toRow(saved));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifAbandonAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Vue liste / détail sans graphe JPA (évite LazyInitializationException à la sérialisation).
	 */
	private Map<String, Object> toRow(EffectifAbandonAlpha e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		m.put("idPeriodeActivite", JpaAssociationIds.intIdOrNull(e.getIdPeriodeActivite()));
		m.put("idCentre", JpaAssociationIds.intIdOrNull(e.getIdCentre()));
		m.put("codeEffectifAbandonAlpha", e.getCodeEffectifAbandonAlpha());
		m.put("effectifAbandonAlphaNiveauHomme", e.getEffectifAbandonAlphaNiveauHomme());
		m.put("effectifAbandonAlphaNiveauFemme", e.getEffectifAbandonAlphaNiveauFemme());
		m.put("effectifAbandonAlphaMoins15F", e.getEffectifAbandonAlphaMoins15F());
		m.put("effectifAbandonAlphaMoins15H", e.getEffectifAbandonAlphaMoins15H());
		m.put("effectifAbandonAlphaMoins15IvoirienH", e.getEffectifAbandonAlphaMoins15IvoirienH());
		m.put("effectifAbandonAlphaMoins15IvoirienF", e.getEffectifAbandonAlphaMoins15IvoirienF());
		m.put("effectifAbandonAlphaMoins15HandicapH", e.getEffectifAbandonAlphaMoins15HandicapH());
		m.put("effectifAbandonAlphaMoins15HandicapF", e.getEffectifAbandonAlphaMoins15HandicapF());
		m.put("effectifAbandonAlpha1524F", e.getEffectifAbandonAlpha1524F());
		m.put("effectifAbandonAlpha1524H", e.getEffectifAbandonAlpha1524H());
		m.put("effectifAbandonAlpha1524IvoirienH", e.getEffectifAbandonAlpha1524IvoirienH());
		m.put("effectifAbandonAlpha1524IvoirienF", e.getEffectifAbandonAlpha1524IvoirienF());
		m.put("effectifAbandonAlpha1524HandicapH", e.getEffectifAbandonAlpha1524HandicapH());
		m.put("effectifAbandonAlpha1524HandicapF", e.getEffectifAbandonAlpha1524HandicapF());
		m.put("effectifAbandonAlpha2549F", e.getEffectifAbandonAlpha2549F());
		m.put("effectifAbandonAlpha2549H", e.getEffectifAbandonAlpha2549H());
		m.put("effectifAbandonAlpha2549IvoirienF", e.getEffectifAbandonAlpha2549IvoirienF());
		m.put("effectifAbandonAlpha2549IvoirienH", e.getEffectifAbandonAlpha2549IvoirienH());
		m.put("effectifAbandonAlpha2549HandicapH", e.getEffectifAbandonAlpha2549HandicapH());
		m.put("effectifAbandonAlpha2549HandicapF", e.getEffectifAbandonAlpha2549HandicapF());
		m.put("effectifAbandonAlpha50PlusF", e.getEffectifAbandonAlpha50PlusF());
		m.put("effectifAbandonAlpha50PlusH", e.getEffectifAbandonAlpha50PlusH());
		m.put("effectifAbandonAlpha50PlusIvoirienH", e.getEffectifAbandonAlpha50PlusIvoirienH());
		m.put("effectifAbandonAlpha50PlusIvoirienF", e.getEffectifAbandonAlpha50PlusIvoirienF());
		m.put("effectifAbandonAlpha50PlusHandicapH", e.getEffectifAbandonAlpha50PlusHandicapH());
		m.put("effectifAbandonAlpha50PlusHandicapF", e.getEffectifAbandonAlpha50PlusHandicapF());
		m.put("causeAbandonAlpha", e.getCauseAbandonAlpha());
		return m;
	}
}
