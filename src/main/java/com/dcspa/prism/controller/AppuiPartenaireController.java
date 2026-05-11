package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.AppuiPartenaireRequest;
import com.dcspa.prism.entity.AppuiPartenaire;
import com.dcspa.prism.entity.CategorieAppui;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Partenaire;
import com.dcspa.prism.repository.CategorieAppuiRepository;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.PartenaireRepository;
import com.dcspa.prism.service.AppuiPartenaireService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appui-partenaire")
@RequiredArgsConstructor
public class AppuiPartenaireController {

	private final AppuiPartenaireService appuiPartenaireService;
	private final CategorieAppuiRepository categorieAppuiRepository;
	private final CentreRepository centreRepository;
	private final PartenaireRepository partenaireRepository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(appuiPartenaireService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return appuiPartenaireService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional(readOnly = true)
	@GetMapping("/search")
	public ResponseEntity<List<Map<String, Object>>> search(
			@RequestParam(required = false) Integer idCategorieAppui,
			@RequestParam(required = false) Integer idCentre,
			@RequestParam(required = false) Integer idPartenaire,
			@RequestParam(required = false) String code,
			@RequestParam(required = false) String libelle) {
		String c = code == null ? null : code.toLowerCase();
		String l = libelle == null ? null : libelle.toLowerCase();
		return ResponseEntity.ok(appuiPartenaireService.findAll().stream()
				.filter(x -> idCategorieAppui == null || idCategorieAppui.equals(JpaAssociationIds.intIdOrNull(x.getIdCategorieAppui())))
				.filter(x -> idCentre == null || idCentre.equals(JpaAssociationIds.intIdOrNull(x.getIdCentre())))
				.filter(x -> idPartenaire == null || idPartenaire.equals(JpaAssociationIds.intIdOrNull(x.getIdPartenaire())))
				.filter(x -> c == null || (x.getCodeAppuiPartenaire() != null && x.getCodeAppuiPartenaire().toLowerCase().contains(c)))
				.filter(x -> l == null || (x.getLibelleAppuiPartenaire() != null && x.getLibelleAppuiPartenaire().toLowerCase().contains(l)))
				.map(this::toRow)
				.toList());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<?> create(@RequestBody AppuiPartenaireRequest body) {
		try {
			AppuiPartenaire entity = new AppuiPartenaire();
			apply(entity, body);
			return ResponseEntity.status(201).body(toRow(appuiPartenaireService.save(entity)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody AppuiPartenaireRequest body) {
		return appuiPartenaireService.findById(id)
				.map(existing -> {
					try {
						apply(existing, body);
						return ResponseEntity.ok(toRow(appuiPartenaireService.save(existing)));
					} catch (IllegalArgumentException ex) {
						return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
					}
				})
				.orElse(ResponseEntity.notFound().build());
	}

	private void apply(AppuiPartenaire entity, AppuiPartenaireRequest request) {
		if (request == null || request.getIdCategorieAppui() == null) {
			throw new IllegalArgumentException("idCategorieAppui est obligatoire");
		}
		if (request.getIdCentre() == null) {
			throw new IllegalArgumentException("idCentre est obligatoire");
		}
		if (request.getIdPartenaire() == null) {
			throw new IllegalArgumentException("idPartenaire est obligatoire");
		}

		CategorieAppui categorieAppui = categorieAppuiRepository.findById(request.getIdCategorieAppui())
				.orElseThrow(() -> new IllegalArgumentException("CategorieAppui introuvable: " + request.getIdCategorieAppui()));
		Centre centre = centreRepository.findById(request.getIdCentre())
				.orElseThrow(() -> new IllegalArgumentException("Centre introuvable: " + request.getIdCentre()));
		Partenaire partenaire = partenaireRepository.findById(request.getIdPartenaire())
				.orElseThrow(() -> new IllegalArgumentException("Partenaire introuvable: " + request.getIdPartenaire()));

		entity.setIdCategorieAppui(categorieAppui);
		entity.setIdCentre(centre);
		entity.setIdPartenaire(partenaire);
		entity.setLibelleAppuiPartenaire(request.getLibelleAppuiPartenaire());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		appuiPartenaireService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(AppuiPartenaire e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "CategorieAppui", e.getIdCategorieAppui());
		ReferentielEnricher.putRef(m, "Centre", e.getIdCentre());
		ReferentielEnricher.putRef(m, "Partenaire", e.getIdPartenaire());
		m.put("codeAppuiPartenaire", e.getCodeAppuiPartenaire());
		m.put("libelleAppuiPartenaire", e.getLibelleAppuiPartenaire());
		return m;
	}
}
