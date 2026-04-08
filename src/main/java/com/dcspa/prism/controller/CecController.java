package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.dto.CecListFilter;
import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.SimpleCentreCreateRequest;
import com.dcspa.prism.dto.SimpleCentreTypeFullCreateRequest;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.dto.UpdateLibelleRequest;
import com.dcspa.prism.entity.Cec;
import com.dcspa.prism.service.CecService;
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
@RequestMapping("/api/cec")
@RequiredArgsConstructor
public class CecController {

	private final CecService cecService;

	// Liste paginée des CEC (filtres optionnels = paramètres de requête).
	@GetMapping
	public ResponseEntity<Page<CentreTypeListItem>> findAll(
			@PageableDefault(size = 20, sort = "id") Pageable pageable,
			@ModelAttribute CecListFilter filter) {
		return ResponseEntity.ok(cecService.findAllListItems(pageable, filter));
	}

	// Détail d’un CEC par identifiant.
	@GetMapping("/{id}")
	public ResponseEntity<Cec> findById(@PathVariable Integer id) {
		return cecService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Crée un CEC rattaché à un centre existant.
	@PostMapping
	public ResponseEntity<CentreTypeListItem> create(@RequestBody SimpleCentreCreateRequest req) {
		return ResponseEntity.status(201).body(cecService.create(req));
	}

	// Création complète : promoteur, centre puis fiche CEC.
	@PostMapping("/full")
	public ResponseEntity<CentreTypeListItem> createFull(@RequestBody SimpleCentreTypeFullCreateRequest req) {
		return ResponseEntity.status(201).body(cecService.createFull(req));
	}

	// Mise à jour d’un CEC en conservant les champs auto-générés.
	@PutMapping("/{id}")
	public ResponseEntity<Cec> update(@PathVariable Integer id, @RequestBody Cec body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, cecService::findById, cecService::save);
	}

	// Met à jour uniquement le libellé affiché.
	@PutMapping("/{id}/libelle")
	public ResponseEntity<CentreTypeListItem> updateLibelle(@PathVariable Integer id, @RequestBody UpdateLibelleRequest req) {
		return cecService.updateLibelle(id, req)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Met à jour les informations détaillées (localisation, équipements, etc.).
	@PutMapping("/{id}/infos")
	public ResponseEntity<CentreTypeListItem> updateInfos(@PathVariable Integer id, @RequestBody UpdateCentreTypeInfosRequest req) {
		return cecService.updateInfos(id, req)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Supprime un CEC.
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		cecService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
