package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.PerformanceRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.Performance;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.PerformanceRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/performance")
@RequiredArgsConstructor
public class PerformanceController {
	private final PerformanceRepository repository;
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
			@RequestParam(required = false) String tauxFrequentationParMois) {
		String t = tauxFrequentationParMois == null ? null : tauxFrequentationParMois.toLowerCase();
		return ResponseEntity.ok(repository.findAll().stream()
				.filter(x -> idAlpha == null || idAlpha.equals(JpaAssociationIds.intIdOrNull(x.getIdAlpha())))
				.filter(x -> t == null || (x.getTauxFrequentationParMois() != null && x.getTauxFrequentationParMois().toLowerCase().contains(t)))
				.map(this::toRow).toList());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<?> create(@RequestBody PerformanceRequest body) {
		try {
			Performance e = new Performance();
			apply(e, body);
			return ResponseEntity.status(201).body(toRow(repository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody PerformanceRequest body) {
		Optional<Performance> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		try {
			Performance e = opt.get();
			apply(e, body);
			return ResponseEntity.ok(toRow(repository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	private void apply(Performance e, PerformanceRequest r) {
		if (r == null || r.getIdAlpha() == null) throw new IllegalArgumentException("idAlpha est obligatoire");
		Alpha alpha = alphaRepository.findById(r.getIdAlpha()).orElseThrow(() -> new IllegalArgumentException("Alpha introuvable: " + r.getIdAlpha()));
		e.setIdAlpha(alpha);
		e.setTauxFrequentationParMois(r.getTauxFrequentationParMois());
		e.setTauxProgressionApprentissageLecture(r.getTauxProgressionApprentissageLecture());
		e.setTauxProgressionApprentissageEcriture(r.getTauxProgressionApprentissageEcriture());
		e.setTauxProgressionApprentissageCalcul(r.getTauxProgressionApprentissageCalcul());
		e.setTauxProgressionApprentissageCvc(r.getTauxProgressionApprentissageCvc());
	}

	private Map<String, Object> toRow(Performance e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "Alpha", e.getIdAlpha());
		m.put("tauxFrequentationParMois", e.getTauxFrequentationParMois());
		m.put("tauxProgressionApprentissageLecture", e.getTauxProgressionApprentissageLecture());
		m.put("tauxProgressionApprentissageEcriture", e.getTauxProgressionApprentissageEcriture());
		m.put("tauxProgressionApprentissageCalcul", e.getTauxProgressionApprentissageCalcul());
		m.put("tauxProgressionApprentissageCvc", e.getTauxProgressionApprentissageCvc());
		return m;
	}
}
