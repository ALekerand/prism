package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.CpListFilter;
import com.dcspa.prism.dto.SimpleCentreCreateRequest;
import com.dcspa.prism.dto.SimpleCentreTypeFullCreateRequest;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.dto.UpdateLibelleRequest;
import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.service.CpService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cp")
@RequiredArgsConstructor
public class CpController {

	private final CpService cpService;

	// Liste paginée des CP (filtres optionnels = paramètres de requête).
	@GetMapping
	public ResponseEntity<Page<CentreTypeListItem>> findAll(
			@PageableDefault(size = 20, sort = "id") Pageable pageable,
			@ModelAttribute CpListFilter filter) {
		return ResponseEntity.ok(cpService.findAllListItems(pageable, filter));
	}

	// Détail d’un CP par identifiant.
	@GetMapping("/{id}")
	public ResponseEntity<Cp> findById(@PathVariable Integer id) {
		return cpService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Crée un CP rattaché à un centre existant.
	@PostMapping
	public ResponseEntity<CentreTypeListItem> create(@RequestBody SimpleCentreCreateRequest req) {
		return ResponseEntity.status(201).body(cpService.create(req));
	}

	// Création complète : promoteur, centre puis fiche CP.
	@PostMapping("/full")
	public ResponseEntity<CentreTypeListItem> createFull(@RequestBody SimpleCentreTypeFullCreateRequest req) {
		return ResponseEntity.status(201).body(cpService.createFull(req));
	}

	// Mise à jour d’un CP en conservant les champs auto-générés.
	@PutMapping("/{id}")
	public ResponseEntity<Cp> update(@PathVariable Integer id, @RequestBody Cp body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, cpService::findById, cpService::save);
	}

	// Met à jour uniquement le libellé affiché.
	@PutMapping("/{id}/libelle")
	public ResponseEntity<CentreTypeListItem> updateLibelle(@PathVariable Integer id, @RequestBody UpdateLibelleRequest req) {
		return cpService.updateLibelle(id, req)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Met à jour les informations détaillées (localisation, équipements, etc.).
	@PutMapping("/{id}/infos")
	public ResponseEntity<CentreTypeListItem> updateInfos(@PathVariable Integer id, @RequestBody UpdateCentreTypeInfosRequest req) {
		return cpService.updateInfos(id, req)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Supprime un CP.
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		cpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
