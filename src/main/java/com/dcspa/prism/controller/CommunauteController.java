package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.CommunauteRequest;
import com.dcspa.prism.entity.Communaute;
import com.dcspa.prism.entity.Personnemorale;
import com.dcspa.prism.repository.PersonnemoraleRepository;
import com.dcspa.prism.service.CommunauteService;

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
@RequestMapping("/api/communautes")
@RequiredArgsConstructor
public class CommunauteController {

	private final CommunauteService communauteService;
	private final PersonnemoraleRepository personnemoraleRepository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(communauteService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return communauteService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody CommunauteRequest request) {
		return ResponseEntity.status(201).body(toRow(communauteService.save(toEntity(null, request))));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody CommunauteRequest request) {
		return ResponseEntity.ok(toRow(communauteService.save(toEntity(id, request))));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		communauteService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(Communaute c) {
		return new LinkedHashMap<>(ReferentielEnricher.toRef(c));
	}

	private Communaute toEntity(Integer id, CommunauteRequest r) {
		if (r.getIdPromoteur() == null) {
			throw new IllegalArgumentException("idPromoteur est obligatoire.");
		}
		Personnemorale pm = personnemoraleRepository.findById(r.getIdPromoteur())
				.orElseThrow(() -> new IllegalArgumentException("Personne morale introuvable: " + r.getIdPromoteur()));
		Communaute c = new Communaute();
		if (id != null) {
			c.setId(id);
		}
		c.setPersonnemorale(pm);
		c.setLibelleCommunaute(r.getLibelleCommunaute());
		String codePromoteur = r.getCodePromoteur();
		if (id != null) {
			codePromoteur = communauteService.findById(id)
					.map(ex -> AutoCodePutMerge.mergeCodeString(r.getCodePromoteur(), ex.getCodePromoteur()))
					.orElse(codePromoteur);
		}
		c.setCodePromoteur(codePromoteur);
		c.setLibellePromoteur(r.getLibellePromoteur());
		c.setDenomination(r.getDenomination());
		c.setNomProgramme(r.getNomProgramme());
		c.setNomRepresentantLegalStructure(r.getNomRepresentantLegalStructure());
		c.setContact(r.getContact());
		c.setBoitePostale(r.getBoitePostale());
		c.setMail(r.getMail());
		return c;
	}
}
