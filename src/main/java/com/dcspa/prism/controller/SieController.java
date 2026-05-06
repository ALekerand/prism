package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.CentreWithPromoteurItem;
import com.dcspa.prism.dto.CentreSearchRequest;
import com.dcspa.prism.dto.SimpleCentreCreateRequest;
import com.dcspa.prism.dto.SieListFilter;
import com.dcspa.prism.dto.SimpleCentreTypeFullCreateRequest;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.dto.UpdateLibelleRequest;
import com.dcspa.prism.entity.Sie;
import com.dcspa.prism.service.SieService;
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

import java.util.List;

@RestController
@RequestMapping("/api/sie")
@RequiredArgsConstructor
public class SieController {

	private final SieService sieService;

	// Liste paginée des SIE (filtres optionnels = paramètres de requête).
	@GetMapping
	public ResponseEntity<Page<CentreTypeListItem>> findAll(
			@PageableDefault(size = 20, sort = "id") Pageable pageable,
			@ModelAttribute SieListFilter filter) {
		return ResponseEntity.ok(sieService.findAllListItems(pageable, filter));
	}

	// Détail d’un SIE par identifiant.
	@GetMapping("/{id}")
	public ResponseEntity<CentreWithPromoteurItem> findById(@PathVariable Integer id) {
		return sieService.findDetailedById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/search")
	public ResponseEntity<List<CentreWithPromoteurItem>> search(@RequestBody(required = false) CentreSearchRequest request) {
		return ResponseEntity.ok(sieService.searchDetailed(request));
	}

	// Création principale : promoteur, centre puis fiche SIE.
	@PostMapping
	public ResponseEntity<CentreTypeListItem> create(@RequestBody SimpleCentreTypeFullCreateRequest req) {
		return ResponseEntity.status(201).body(sieService.createFull(req));
	}

	// Ancien endpoint simple conservé avec préfixe old.
	@PostMapping("/old")
	public ResponseEntity<CentreTypeListItem> createOld(@RequestBody SimpleCentreCreateRequest req) {
		return ResponseEntity.status(201).body(sieService.create(req));
	}

	// Mise à jour d’un SIE en conservant les champs auto-générés.
	@PutMapping("/{id}")
	public ResponseEntity<Sie> update(@PathVariable Integer id, @RequestBody Sie body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, sieService::findById, sieService::save);
	}

	// Met à jour uniquement le libellé affiché.
	@PutMapping("/{id}/libelle")
	public ResponseEntity<CentreTypeListItem> updateLibelle(@PathVariable Integer id, @RequestBody UpdateLibelleRequest req) {
		return sieService.updateLibelle(id, req)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Met à jour les informations détaillées (localisation, équipements, etc.).
	@PutMapping("/{id}/infos")
	public ResponseEntity<CentreTypeListItem> updateInfos(@PathVariable Integer id, @RequestBody UpdateCentreTypeInfosRequest req) {
		return sieService.updateInfos(id, req)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Supprime un SIE.
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		sieService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
