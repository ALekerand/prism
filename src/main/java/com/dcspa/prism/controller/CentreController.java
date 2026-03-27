package com.dcspa.prism.controller;

import com.dcspa.prism.dto.CentreRequest;
import com.dcspa.prism.entity.AutoriteAutorisation;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Periodicite;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.repository.AutoriteAutorisationRepository;
import com.dcspa.prism.repository.PeriodiciteRepository;
import com.dcspa.prism.repository.PromoteurRepository;
import com.dcspa.prism.service.CentreService;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/centres")
@RequiredArgsConstructor
public class CentreController {

	private final CentreService centreService;
	private final PromoteurRepository promoteurRepository;
	private final PeriodiciteRepository periodiciteRepository;
	private final AutoriteAutorisationRepository autoriteAutorisationRepository;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		// Evite les LazyInitializationException sur les associations (Iep, Localite, Promoteur...)
		// Le frontend a besoin de { id, codeCentre } pour la sélection.
		List<Map<String, Object>> list = centreService.findAll().stream()
				.map(c -> Map.<String, Object>of(
						"id", c.getId(),
						"codeCentre", c.getCodeCentre()
				))
				.collect(Collectors.toList());
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Centre> findById(@PathVariable Integer id) {
		return centreService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Centre> create(@RequestBody CentreRequest request) {
		Centre saved = centreService.save(toEntity(null, request));
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Centre> update(@PathVariable Integer id, @RequestBody CentreRequest request) {
		Centre saved = centreService.save(toEntity(id, request));
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		centreService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Centre toEntity(Integer id, CentreRequest r) {
		Centre c = new Centre();
		c.setId(id);

		Promoteur p = promoteurRepository.findById(r.getIdPromoteur())
				.orElseThrow(() -> new IllegalArgumentException("Promoteur introuvable: " + r.getIdPromoteur()));
		c.setIdPromoteur(p);

		if (r.getIdPeriodicite() != null) {
			Periodicite per = periodiciteRepository.findById(r.getIdPeriodicite().longValue())
					.orElseThrow(() -> new IllegalArgumentException("Periodicite introuvable: " + r.getIdPeriodicite()));
			c.setIdPeriodicite(per);
		}

		if (r.getIdAutoriteAutorisation() != null) {
			AutoriteAutorisation a = autoriteAutorisationRepository.findById(r.getIdAutoriteAutorisation())
					.orElseThrow(() -> new IllegalArgumentException("AutoriteAutorisation introuvable: " + r.getIdAutoriteAutorisation()));
			c.setIdAutoriteAutorisation(a);
		}

		c.setCodeCentre(r.getCodeCentre());
		c.setAutorisation(r.getAutorisation());
		c.setEncadreurNonMena(r.getEncadreurNonMena());
		c.setEncadrerParMena(r.getEncadrerParMena());
		c.setEstElectrifie(r.getEstElectrifie());
		c.setADeLeau(r.getADeLeau());
		c.setNombreVisite(r.getNombreVisite());
		return c;
	}
}
