package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.VisiteRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.Visite;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.VisiteRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visite")
@RequiredArgsConstructor
public class VisiteController {
	private final VisiteRepository repository;
	private final AlphaRepository alphaRepository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(repository.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return repository.findById(id).map(this::toRow).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Transactional(readOnly = true)
	@GetMapping("/search")
	public ResponseEntity<List<Map<String, Object>>> search(@RequestParam(required = false) Integer idAlpha,
			@RequestParam(required = false) String maitriseSeanceLecture) {
		String m = maitriseSeanceLecture == null ? null : maitriseSeanceLecture.toLowerCase();
		return ResponseEntity.ok(repository.findAll().stream()
				.filter(x -> idAlpha == null || idAlpha.equals(JpaAssociationIds.intIdOrNull(x.getIdAlpha())))
				.filter(x -> m == null || (x.getMaitriseSeanceLecture() != null && x.getMaitriseSeanceLecture().toLowerCase().contains(m)))
				.map(this::toRow).toList());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<?> create(@RequestBody VisiteRequest body) {
		try {
			Visite e = new Visite();
			apply(e, body);
			return ResponseEntity.status(201).body(toRow(repository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody VisiteRequest body) {
		Optional<Visite> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		try {
			Visite e = opt.get();
			apply(e, body);
			return ResponseEntity.ok(toRow(repository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	private void apply(Visite e, VisiteRequest r) {
		if (r == null || r.getIdAlpha() == null) throw new IllegalArgumentException("idAlpha est obligatoire");
		Alpha alpha = alphaRepository.findById(r.getIdAlpha()).orElseThrow(() -> new IllegalArgumentException("Alpha introuvable: " + r.getIdAlpha()));
		e.setIdAlpha(alpha);
		e.setMaitriseSeanceLecture(r.getMaitriseSeanceLecture());
		e.setMaitriseSeanceEcriture(r.getMaitriseSeanceEcriture());
		e.setMaitriseSeanceCalcul(r.getMaitriseSeanceCalcul());
		e.setMaitriseSeanceCvc(r.getMaitriseSeanceCvc());
		e.setNombreVisiteRealiseParConseiller(r.getNombreVisiteRealiseParConseiller());
		e.setNombreBulletinEffectueParConseiller(r.getNombreBulletinEffectueParConseiller());
		e.setNombreVisiteConseillerSuperviseurEffectue(r.getNombreVisiteConseillerSuperviseurEffectue());
		e.setNombreReunionBilanConseillerSuperviseur(r.getNombreReunionBilanConseillerSuperviseur());
		e.setNombreVisiteEffectueParIepp(r.getNombreVisiteEffectueParIepp());
		e.setNombreReunionPointActiviteAlpha(r.getNombreReunionPointActiviteAlpha());
	}

	private Map<String, Object> toRow(Visite e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "Alpha", e.getIdAlpha());
		m.put("maitriseSeanceLecture", e.getMaitriseSeanceLecture());
		m.put("maitriseSeanceEcriture", e.getMaitriseSeanceEcriture());
		m.put("maitriseSeanceCalcul", e.getMaitriseSeanceCalcul());
		m.put("maitriseSeanceCvc", e.getMaitriseSeanceCvc());
		m.put("nombreVisiteRealiseParConseiller", e.getNombreVisiteRealiseParConseiller());
		m.put("nombreBulletinEffectueParConseiller", e.getNombreBulletinEffectueParConseiller());
		m.put("nombreVisiteConseillerSuperviseurEffectue", e.getNombreVisiteConseillerSuperviseurEffectue());
		m.put("nombreReunionBilanConseillerSuperviseur", e.getNombreReunionBilanConseillerSuperviseur());
		m.put("nombreVisiteEffectueParIepp", e.getNombreVisiteEffectueParIepp());
		m.put("nombreReunionPointActiviteAlpha", e.getNombreReunionPointActiviteAlpha());
		return m;
	}
}
