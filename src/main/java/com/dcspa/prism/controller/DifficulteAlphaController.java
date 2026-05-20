package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.LiaisonCatalogSyncRequest;
import com.dcspa.prism.entity.DifficulteAlpha;
import com.dcspa.prism.service.CentreLiaisonSyncService;
import com.dcspa.prism.service.DifficulteAlphaService;
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
@RequestMapping("/api/difficulte-alpha")
@RequiredArgsConstructor
public class DifficulteAlphaController {

	private final DifficulteAlphaService difficulteAlphaService;
	private final CentreLiaisonSyncService centreLiaisonSyncService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(difficulteAlphaService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return difficulteAlphaService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody DifficulteAlpha body) {
		return ResponseEntity.status(201).body(toRow(difficulteAlphaService.save(body)));
	}

	@Transactional
	@PostMapping("/sync")
	public ResponseEntity<Void> sync(@RequestBody LiaisonCatalogSyncRequest request) {
		centreLiaisonSyncService.syncDifficulteAlpha(request);
		return ResponseEntity.noContent().build();
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody DifficulteAlpha body) {
		Optional<DifficulteAlpha> opt = difficulteAlphaService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		body.setId(id);
		return ResponseEntity.ok(toRow(difficulteAlphaService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		difficulteAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(DifficulteAlpha e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "Difficulte", e.getIdDifficulte());
		ReferentielEnricher.putRef(m, "Alpha", e.getIdCentre());
		m.put("codeDifficulteAlpha", e.getCodeDifficulteAlpha());
		return m;
	}
}
