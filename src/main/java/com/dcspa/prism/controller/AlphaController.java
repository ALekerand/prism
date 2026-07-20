package com.dcspa.prism.controller;

import com.dcspa.prism.dto.AlphaCreateRequest;
import com.dcspa.prism.dto.AlphaFullCreateRequest;
import com.dcspa.prism.dto.AlphaListFilter;
import com.dcspa.prism.dto.CentreActifUpdateRequest;
import com.dcspa.prism.dto.CentreSearchRequest;
import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.CentreWithPromoteurItem;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.dto.UpdateLibelleRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.AlphaService;
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
@RequestMapping("/api/alpha")
@RequiredArgsConstructor
public class AlphaController {

	private final AlphaService alphaService;

	// Liste paginée des centres Alpha (filtres optionnels = paramètres de requête).
	@GetMapping
	public ResponseEntity<Page<CentreTypeListItem>> findAll(
			@PageableDefault(size = 20, sort = "id") Pageable pageable,
			@ModelAttribute AlphaListFilter filter,
			@AuthenticationPrincipal AuthUser user) {
		return ResponseEntity.ok(alphaService.findAllListItems(pageable, filter, user));
	}

	// Détail d’un enregistrement Alpha par identifiant.
	@GetMapping("/{id}")
	public ResponseEntity<CentreWithPromoteurItem> findById(
			@PathVariable Integer id,
			@AuthenticationPrincipal AuthUser user) {
		return alphaService.findDetailedById(id, user)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/search")
	public ResponseEntity<List<CentreWithPromoteurItem>> search(
			@RequestBody(required = false) CentreSearchRequest request,
			@AuthenticationPrincipal AuthUser user) {
		return ResponseEntity.ok(alphaService.searchDetailed(request, user));
	}

	// Création principale : promoteur, centre puis Alpha.
	@PostMapping
	public ResponseEntity<CentreTypeListItem> create(
			@RequestBody AlphaFullCreateRequest req,
			@AuthenticationPrincipal AuthUser user) {
		return ResponseEntity.status(201).body(alphaService.createFull(req, user));
	}

	// Ancien endpoint simple conservé avec préfixe old.
	@PostMapping("/old")
	public ResponseEntity<CentreTypeListItem> createOld(
			@RequestBody AlphaCreateRequest req,
			@AuthenticationPrincipal AuthUser user) {
		return ResponseEntity.status(201).body(alphaService.create(req, user));
	}

	// Mise à jour d’un Alpha en conservant les champs auto-générés.
	@PutMapping("/{id}")
	public ResponseEntity<Alpha> update(
			@PathVariable Integer id,
			@RequestBody Alpha alpha,
			@AuthenticationPrincipal AuthUser user) {
		return alphaService.putPreservingAutoCode(id, alpha, user)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Met à jour uniquement le libellé affiché.
	@PutMapping("/{id}/libelle")
	public ResponseEntity<CentreTypeListItem> updateLibelle(
			@PathVariable Integer id,
			@RequestBody UpdateLibelleRequest req,
			@AuthenticationPrincipal AuthUser user) {
		return alphaService.updateLibelle(id, req, user)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Met à jour les informations détaillées (localisation, équipements, etc.).
	@PutMapping("/{id}/infos")
	public ResponseEntity<CentreTypeListItem> updateInfos(
			@PathVariable Integer id,
			@RequestBody UpdateCentreTypeInfosRequest req,
			@AuthenticationPrincipal AuthUser user) {
		return alphaService.updateInfos(id, req, user)
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
		return alphaService.updateActif(id, req.getActif(), user)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Supprime un centre Alpha.
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		if (!alphaService.deleteById(id, user)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
}

