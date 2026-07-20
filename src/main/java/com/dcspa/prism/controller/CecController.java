package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.dto.CecListFilter;
import com.dcspa.prism.dto.CentreActifUpdateRequest;
import com.dcspa.prism.dto.CentreSearchRequest;
import com.dcspa.prism.dto.CentreWithPromoteurItem;
import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.SimpleCentreCreateRequest;
import com.dcspa.prism.dto.SimpleCentreTypeFullCreateRequest;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.dto.UpdateLibelleRequest;
import com.dcspa.prism.entity.Cec;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.CecService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cec")
@RequiredArgsConstructor
public class CecController {

	private final CecService cecService;

	// Liste paginée des CEC (filtres optionnels = paramètres de requête).
	@GetMapping
	public ResponseEntity<Page<CentreTypeListItem>> findAll(
			@PageableDefault(size = 20, sort = "id") Pageable pageable,
			@ModelAttribute CecListFilter filter,
			@AuthenticationPrincipal AuthUser user) {
		return ResponseEntity.ok(cecService.findAllListItems(pageable, filter, user));
	}

	// Détail d’un CEC par identifiant.
	@GetMapping("/{id}")
	public ResponseEntity<CentreWithPromoteurItem> findById(
			@PathVariable Integer id,
			@AuthenticationPrincipal AuthUser user) {
		return cecService.findDetailedById(id, user)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/search")
	public ResponseEntity<List<CentreWithPromoteurItem>> search(
			@RequestBody(required = false) CentreSearchRequest request,
			@AuthenticationPrincipal AuthUser user) {
		return ResponseEntity.ok(cecService.searchDetailed(request, user));
	}

	// Création principale : promoteur, centre puis fiche CEC.
	@PostMapping
	public ResponseEntity<CentreTypeListItem> create(
			@RequestBody SimpleCentreTypeFullCreateRequest req,
			@AuthenticationPrincipal AuthUser user) {
		return ResponseEntity.status(201).body(cecService.createFull(req, user));
	}

	// Ancien endpoint simple conservé avec préfixe old.
	@PostMapping("/old")
	public ResponseEntity<CentreTypeListItem> createOld(
			@RequestBody SimpleCentreCreateRequest req,
			@AuthenticationPrincipal AuthUser user) {
		return ResponseEntity.status(201).body(cecService.create(req, user));
	}

	// Mise à jour d’un CEC en conservant les champs auto-générés.
	@PutMapping("/{id}")
	public ResponseEntity<Cec> update(@PathVariable Integer id, @RequestBody Cec body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, cecService::findById, cecService::save);
	}

	// Met à jour uniquement le libellé affiché.
	@PutMapping("/{id}/libelle")
	public ResponseEntity<CentreTypeListItem> updateLibelle(
			@PathVariable Integer id,
			@RequestBody UpdateLibelleRequest req,
			@AuthenticationPrincipal AuthUser user) {
		return cecService.updateLibelle(id, req, user)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Met à jour les informations détaillées (localisation, équipements, etc.).
	@PutMapping("/{id}/infos")
	public ResponseEntity<CentreTypeListItem> updateInfos(
			@PathVariable Integer id,
			@RequestBody UpdateCentreTypeInfosRequest req,
			@AuthenticationPrincipal AuthUser user) {
		return cecService.updateInfos(id, req, user)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@RequestMapping(value = "/{id}/actif", method = {RequestMethod.PUT, RequestMethod.PATCH})
	public ResponseEntity<CentreTypeListItem> updateActif(
			@PathVariable Integer id,
			@RequestBody CentreActifUpdateRequest req,
			@AuthenticationPrincipal AuthUser user) {
		if (req == null || req.getActif() == null) {
			return ResponseEntity.badRequest().build();
		}
		return cecService.updateActif(id, req.getActif(), user)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Supprime un CEC.
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		if (!cecService.deleteById(id, user)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
}
