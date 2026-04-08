package com.dcspa.prism.controller;
import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.dto.AlphaCreateRequest;
import com.dcspa.prism.dto.AlphaFullCreateRequest;
import com.dcspa.prism.dto.AlphaListFilter;
import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.dto.UpdateLibelleRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.service.AlphaService;
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
@RequestMapping("/api/v1/alpha")
@RequiredArgsConstructor
public class AlphaController {

	private final AlphaService alphaService;

	// Liste paginée des centres Alpha (filtres optionnels = paramètres de requête).
	@GetMapping
	public ResponseEntity<Page<CentreTypeListItem>> findAll(
			@PageableDefault(size = 20, sort = "id") Pageable pageable,
			@ModelAttribute AlphaListFilter filter) {
		return ResponseEntity.ok(alphaService.findAllListItems(pageable, filter));
	}

	// Détail d’un enregistrement Alpha par identifiant.
	@GetMapping("/{id}")
	public ResponseEntity<Alpha> findById(@PathVariable Integer id) {
		return alphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Crée un Alpha lié à un centre et aux références métier (campagne, type, etc.).
	@PostMapping
	public ResponseEntity<CentreTypeListItem> create(@RequestBody AlphaCreateRequest req) {
		return ResponseEntity.status(201).body(alphaService.create(req));
	}

	// Création complète : promoteur, centre puis Alpha (codes auto côté persistance).
	@PostMapping("/full")
	public ResponseEntity<CentreTypeListItem> createFull(@RequestBody AlphaFullCreateRequest req) {
		return ResponseEntity.status(201).body(alphaService.createFull(req));
	}

	// Mise à jour d’un Alpha en conservant les champs auto-générés.
	@PutMapping("/{id}")
	public ResponseEntity<Alpha> update(@PathVariable Integer id, @RequestBody Alpha alpha) {
		return ReferentialPutHelper.putPreservingAutoCode(id, alpha, alphaService::findById, alphaService::save);
	}

	// Met à jour uniquement le libellé affiché.
	@PutMapping("/{id}/libelle")
	public ResponseEntity<CentreTypeListItem> updateLibelle(@PathVariable Integer id, @RequestBody UpdateLibelleRequest req) {
		return alphaService.updateLibelle(id, req)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Met à jour les informations détaillées (localisation, équipements, etc.).
	@PutMapping("/{id}/infos")
	public ResponseEntity<CentreTypeListItem> updateInfos(@PathVariable Integer id, @RequestBody UpdateCentreTypeInfosRequest req) {
		return alphaService.updateInfos(id, req)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Supprime un centre Alpha.
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		alphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
