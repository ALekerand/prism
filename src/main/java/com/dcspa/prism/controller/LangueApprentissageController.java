package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.LangueApprentissageRequest;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.LangueApprentissage;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.service.LangueApprentissageService;
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
@RequestMapping("/api/LangueApprentissages")
@RequiredArgsConstructor
public class LangueApprentissageController {

	private final LangueApprentissageService langueApprentissageService;
	private final CentreRepository centreRepository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(langueApprentissageService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return langueApprentissageService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody LangueApprentissageRequest request) {
		return ResponseEntity.status(201).body(toRow(langueApprentissageService.save(toEntity(null, request))));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(
			@PathVariable Integer id, @RequestBody LangueApprentissageRequest request) {
		return ResponseEntity.ok(toRow(langueApprentissageService.save(toEntity(id, request))));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		langueApprentissageService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(LangueApprentissage e) {
		return new LinkedHashMap<>(ReferentielEnricher.toRef(e));
	}

	private LangueApprentissage toEntity(Integer id, LangueApprentissageRequest r) {
		if (r.getIdCentre() == null) {
			throw new IllegalArgumentException("idCentre est obligatoire.");
		}
		LangueApprentissage la = new LangueApprentissage();
		la.setId(id);
		Centre centre = centreRepository.findById(r.getIdCentre())
				.orElseThrow(() -> new IllegalArgumentException("Centre introuvable: " + r.getIdCentre()));
		la.setIdCentre(centre);
		la.setLibelleLangue(r.getLibelleLangue());
		return la;
	}
}
