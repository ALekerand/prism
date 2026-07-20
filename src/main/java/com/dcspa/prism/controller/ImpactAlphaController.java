package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.LiaisonCatalogSyncRequest;
import com.dcspa.prism.entity.ImpactAlpha;
import com.dcspa.prism.service.CentreLiaisonSyncService;
import com.dcspa.prism.service.ImpactAlphaService;
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
@RequestMapping("/api/impact-alpha")
@RequiredArgsConstructor
public class ImpactAlphaController {

	private final ImpactAlphaService impactAlphaService;
	private final CentreLiaisonSyncService centreLiaisonSyncService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(impactAlphaService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return impactAlphaService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody ImpactAlpha body) {
		return ResponseEntity.status(201).body(toRow(impactAlphaService.save(body)));
	}

	@Transactional
	@PostMapping("/sync")
	public ResponseEntity<Void> sync(@RequestBody LiaisonCatalogSyncRequest request) {
		centreLiaisonSyncService.syncImpactAlpha(request);
		return ResponseEntity.noContent().build();
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody ImpactAlpha body) {
		Optional<ImpactAlpha> opt = impactAlphaService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		body.setId(id);
		return ResponseEntity.ok(toRow(impactAlphaService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		impactAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(ImpactAlpha e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "Impact", e.getIdImpact());
		ReferentielEnricher.putRef(m, "Centre", e.getIdCentre());
		m.put("codeImpactAlpha", e.getCodeImpactAlpha());
		return m;
	}
}
