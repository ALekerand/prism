package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.LiaisonCatalogSyncRequest;
import com.dcspa.prism.dto.MaterielAlphaRequest;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.MaterielAlpha;
import com.dcspa.prism.entity.MaterielsPedagogique;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.MaterielsPedagogiqueRepository;
import com.dcspa.prism.service.CentreLiaisonSyncService;
import com.dcspa.prism.service.MaterielAlphaService;
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

@RestController
@RequestMapping("/api/materielalpha")
@RequiredArgsConstructor
public class MaterielAlphaController {

	private final MaterielAlphaService materielalphaService;
	private final CentreLiaisonSyncService centreLiaisonSyncService;
	private final CentreRepository centreRepository;
	private final MaterielsPedagogiqueRepository materielsPedagogiqueRepository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(materielalphaService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return materielalphaService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody MaterielAlphaRequest request) {
		return ResponseEntity.status(201).body(toRow(materielalphaService.save(toEntity(null, request))));
	}

	@Transactional
	@PostMapping("/sync")
	public ResponseEntity<Void> sync(@RequestBody LiaisonCatalogSyncRequest request) {
		centreLiaisonSyncService.syncMaterielAlpha(request);
		return ResponseEntity.noContent().build();
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(
			@PathVariable Integer id, @RequestBody MaterielAlphaRequest request) {
		return materielalphaService.findById(id)
				.map(existing -> ResponseEntity.ok(toRow(materielalphaService.save(toEntity(id, request)))))
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		materielalphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private MaterielAlpha toEntity(Integer id, MaterielAlphaRequest r) {
		MaterielAlpha m = new MaterielAlpha();
		m.setId(id);
		Centre centre = centreRepository.findById(r.getIdCentre())
				.orElseThrow(() -> new IllegalArgumentException("Centre introuvable: " + r.getIdCentre()));
		MaterielsPedagogique mp = materielsPedagogiqueRepository.findById(r.getIdMaterielPedagogique())
				.orElseThrow(() -> new IllegalArgumentException(
						"MaterielPedagogique introuvable: " + r.getIdMaterielPedagogique()));
		m.setIdCentre(centre);
		m.setIdMaterielPedagogique(mp);
		m.setLibelleAutreMateriel(r.getLibelleAutreMateriel());
		return m;
	}

	private Map<String, Object> toRow(MaterielAlpha e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "MaterielPedagogique", e.getIdMaterielPedagogique());
		ReferentielEnricher.putRef(m, "Centre", e.getIdCentre());
		m.put("libelleAutreMateriel", e.getLibelleAutreMateriel());
		return m;
	}
}
