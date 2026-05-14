package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.entity.Personnemorale;
import com.dcspa.prism.entity.Personnephysique;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.repository.PersonnemoraleRepository;
import com.dcspa.prism.repository.PersonnephysiqueRepository;
import com.dcspa.prism.service.PromoteurService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/promoteur")
@RequiredArgsConstructor
public class PromoteurController {

	private final PromoteurService promoteurService;
	private final PersonnephysiqueRepository personnephysiqueRepository;
	private final PersonnemoraleRepository personnemoraleRepository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(promoteurService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/paged")
	public ResponseEntity<Page<Map<String, Object>>> findAllPaged(
			@PageableDefault(size = 20, sort = "id") Pageable pageable) {
		return ResponseEntity.ok(promoteurService.findAll(pageable).map(this::toRow));
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return promoteurService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody Promoteur body) {
		return ResponseEntity.status(201).body(toRow(promoteurService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody Promoteur body) {
		Optional<Promoteur> opt = promoteurService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(promoteurService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		promoteurService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(Promoteur p) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", p.getId());
		m.put("idPromoteur", p.getId());
		m.put("codePromoteur", p.getCodePromoteur());
		m.put("libellePromoteur", p.getLibellePromoteur());
		m.put("typePromoteur", p.getTypePromoteur());
		personnephysiqueRepository.findById(p.getId()).ifPresent(pp -> m.put("personnePhysique", toPersonnePhysiqueRow(pp)));
		personnemoraleRepository.findById(p.getId()).ifPresent(pm -> m.put("personneMorale", toPersonneMoraleRow(pm)));
		return m;
	}

	private Map<String, Object> toPersonnePhysiqueRow(Personnephysique pp) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("libellePersonnePhysique", pp.getLibellePersonnePhysique());
		m.put("nom", pp.getNom());
		m.put("prenom", pp.getPrenom());
		m.put("contact", pp.getContact());
		m.put("fonction", pp.getFonction());
		m.put("sexe", pp.getSexe());
		m.put("dateNaissance", pp.getDateNaissance());
		m.put("anciennete", pp.getAnciennete());
		m.put("boitePostale", pp.getBoitePostale());
		m.put("niveauEtudes", pp.getNiveauEtudes());
		m.put("civilite", pp.getCivilite());
		return m;
	}

	private Map<String, Object> toPersonneMoraleRow(Personnemorale pm) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("denomination", pm.getDenomination());
		m.put("nomProgramme", pm.getNomProgramme());
		m.put("nomRepresentant", pm.getNomRepresentantLegalStructure());
		m.put("contact", pm.getContact());
		m.put("boitePostale", pm.getBoitePostale());
		m.put("mail", pm.getMail());
		m.put("idTypePersonneMorale", pm.getTypePersonneMorale() != null ? pm.getTypePersonneMorale().getId() : null);
		m.put("libelleTypePersonneMorale", pm.getTypePersonneMorale() != null ? pm.getTypePersonneMorale().getLibelle() : null);
		return m;
	}
}
