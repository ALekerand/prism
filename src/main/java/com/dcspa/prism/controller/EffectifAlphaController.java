package com.dcspa.prism.controller;

import com.dcspa.prism.dto.EffectifAlphaRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.EffectifAlpha;
import com.dcspa.prism.entity.NiveauAlpha;
import com.dcspa.prism.entity.PeriodeActivite;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.NiveauAlphaRepository;
import com.dcspa.prism.repository.PeriodeActiviteRepository;
import com.dcspa.prism.service.EffectifAlphaService;
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
@RequestMapping("/api/effectif-alpha")
@RequiredArgsConstructor
public class EffectifAlphaController {

	private final EffectifAlphaService effectifAlphaService;
	private final PeriodeActiviteRepository periodeActiviteRepository;
	private final AlphaRepository alphaRepository;
	private final NiveauAlphaRepository niveauAlphaRepository;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		List<Map<String, Object>> rows = effectifAlphaService.findAll()
				.stream()
				.map(this::toRow)
				.collect(Collectors.toList());
		return ResponseEntity.ok(rows);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return effectifAlphaService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody EffectifAlphaRequest body) {
		try {
			EffectifAlpha entity = new EffectifAlpha();
			applyRequest(entity, body);
			EffectifAlpha saved = effectifAlphaService.save(entity);
			return ResponseEntity.status(201).body(toRow(saved));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody EffectifAlphaRequest body) {
		Optional<EffectifAlpha> opt = effectifAlphaService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		try {
			EffectifAlpha entity = opt.get();
			applyRequest(entity, body);
			EffectifAlpha saved = effectifAlphaService.save(entity);
			return ResponseEntity.ok(toRow(saved));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private void applyRequest(EffectifAlpha e, EffectifAlphaRequest r) {
		if (r == null) {
			throw new IllegalArgumentException("Requête obligatoire");
		}
		if (r.getIdPeriodeActivite() == null) {
			throw new IllegalArgumentException("idPeriodeActivite est obligatoire");
		}
		if (r.getIdCentre() == null) {
			throw new IllegalArgumentException("idCentre est obligatoire");
		}

		PeriodeActivite periode = periodeActiviteRepository.findById(r.getIdPeriodeActivite().longValue())
				.orElseThrow(() -> new IllegalArgumentException("Période introuvable: " + r.getIdPeriodeActivite()));
		Alpha centre = alphaRepository.findById(r.getIdCentre())
				.orElseThrow(() -> new IllegalArgumentException("Centre alpha introuvable: " + r.getIdCentre()));
		NiveauAlpha niveauAlpha = null;
		if (r.getIdNiveauAlpha() != null) {
			niveauAlpha = niveauAlphaRepository.findById(r.getIdNiveauAlpha().longValue())
					.orElseThrow(() -> new IllegalArgumentException("Niveau alpha introuvable: " + r.getIdNiveauAlpha()));
		}

		e.setIdPeriodeActivite(periode);
		e.setIdCentre(centre);
		e.setIdNiveauAlpha(niveauAlpha);
		e.setCodeEffectifAlpha(r.getCodeEffectifAlpha());
		e.setEffectifAlphaNiveauH(r.getEffectifAlphaNiveauH());
		e.setEffectifAlphaNiveauF(r.getEffectifAlphaNiveauF());
		e.setEffectifAlphaMoins15F(r.getEffectifAlphaMoins15F());
		e.setEffectifAlphaMoins15H(r.getEffectifAlphaMoins15H());
		e.setEffectifAlphaMoins15IvoirienH(r.getEffectifAlphaMoins15IvoirienH());
		e.setEffectifAlphaMoins15IvoirienF(r.getEffectifAlphaMoins15IvoirienF());
		e.setEffectifAlphaMoins15HandicapH(r.getEffectifAlphaMoins15HandicapH());
		e.setEffectifAlphaMoins15HandicapF(r.getEffectifAlphaMoins15HandicapF());
		e.setEffectifAlpha1524F(r.getEffectifAlpha1524F());
		e.setEffectifAlpha1524H(r.getEffectifAlpha1524H());
		e.setEffectifAlpha1524IvoirienH(r.getEffectifAlpha1524IvoirienH());
		e.setEffectifAlpha1524IvoirienF(r.getEffectifAlpha1524IvoirienF());
		e.setEffectifAlpha1524HandicapH(r.getEffectifAlpha1524HandicapH());
		e.setEffectifAlpha1524HandicapF(r.getEffectifAlpha1524HandicapF());
		e.setEffectifAlpha2549F(r.getEffectifAlpha2549F());
		e.setEffectifAlpha2549H(r.getEffectifAlpha2549H());
		e.setEffectifAlpha2549IvoirienF(r.getEffectifAlpha2549IvoirienF());
		e.setEffectifAlpha2549IvoirienH(r.getEffectifAlpha2549IvoirienH());
		e.setEffectifAlpha2549HandicapH(r.getEffectifAlpha2549HandicapH());
		e.setEffectifAlpha2549HandicapF(r.getEffectifAlpha2549HandicapF());
		e.setEffectifAlpha50PlusF(r.getEffectifAlpha50PlusF());
		e.setEffectifAlpha50PlusH(r.getEffectifAlpha50PlusH());
		e.setEffectifAlpha50PlusIvoirienH(r.getEffectifAlpha50PlusIvoirienH());
		e.setEffectifAlpha50PlusIvoirienF(r.getEffectifAlpha50PlusIvoirienF());
		e.setEffectifAlpha50PlusHandicapH(r.getEffectifAlpha50PlusHandicapH());
		e.setEffectifAlpha50PlusHandicapF(r.getEffectifAlpha50PlusHandicapF());
	}

	private Map<String, Object> toRow(EffectifAlpha e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		m.put("idPeriodeActivite", e.getIdPeriodeActivite() != null ? e.getIdPeriodeActivite().getId() : null);
		m.put("libellePeriodeActivite", e.getIdPeriodeActivite() != null ? e.getIdPeriodeActivite().getLibellePeriodeActivite() : null);
		m.put("idCentre", e.getIdCentre() != null ? e.getIdCentre().getId() : null);
		m.put("libelleCentre", e.getIdCentre() != null ? e.getIdCentre().getLibelleAlpha() : null);
		m.put("idNiveauAlpha", e.getIdNiveauAlpha() != null ? e.getIdNiveauAlpha().getId() : null);
		m.put("libelleNiveauAlpha", e.getIdNiveauAlpha() != null ? e.getIdNiveauAlpha().getLibelleNiveauAlpha() : null);
		m.put("codeEffectifAlpha", e.getCodeEffectifAlpha());
		m.put("effectifAlphaNiveauH", e.getEffectifAlphaNiveauH());
		m.put("effectifAlphaNiveauF", e.getEffectifAlphaNiveauF());
		m.put("effectifAlphaMoins15F", e.getEffectifAlphaMoins15F());
		m.put("effectifAlphaMoins15H", e.getEffectifAlphaMoins15H());
		m.put("effectifAlphaMoins15IvoirienH", e.getEffectifAlphaMoins15IvoirienH());
		m.put("effectifAlphaMoins15IvoirienF", e.getEffectifAlphaMoins15IvoirienF());
		m.put("effectifAlphaMoins15HandicapH", e.getEffectifAlphaMoins15HandicapH());
		m.put("effectifAlphaMoins15HandicapF", e.getEffectifAlphaMoins15HandicapF());
		m.put("effectifAlpha1524F", e.getEffectifAlpha1524F());
		m.put("effectifAlpha1524H", e.getEffectifAlpha1524H());
		m.put("effectifAlpha1524IvoirienH", e.getEffectifAlpha1524IvoirienH());
		m.put("effectifAlpha1524IvoirienF", e.getEffectifAlpha1524IvoirienF());
		m.put("effectifAlpha1524HandicapH", e.getEffectifAlpha1524HandicapH());
		m.put("effectifAlpha1524HandicapF", e.getEffectifAlpha1524HandicapF());
		m.put("effectifAlpha2549F", e.getEffectifAlpha2549F());
		m.put("effectifAlpha2549H", e.getEffectifAlpha2549H());
		m.put("effectifAlpha2549IvoirienF", e.getEffectifAlpha2549IvoirienF());
		m.put("effectifAlpha2549IvoirienH", e.getEffectifAlpha2549IvoirienH());
		m.put("effectifAlpha2549HandicapH", e.getEffectifAlpha2549HandicapH());
		m.put("effectifAlpha2549HandicapF", e.getEffectifAlpha2549HandicapF());
		m.put("effectifAlpha50PlusF", e.getEffectifAlpha50PlusF());
		m.put("effectifAlpha50PlusH", e.getEffectifAlpha50PlusH());
		m.put("effectifAlpha50PlusIvoirienH", e.getEffectifAlpha50PlusIvoirienH());
		m.put("effectifAlpha50PlusIvoirienF", e.getEffectifAlpha50PlusIvoirienF());
		m.put("effectifAlpha50PlusHandicapH", e.getEffectifAlpha50PlusHandicapH());
		m.put("effectifAlpha50PlusHandicapF", e.getEffectifAlpha50PlusHandicapF());
		return m;
	}
}
