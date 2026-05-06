package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;
import com.dcspa.prism.dto.MinistereRequest;
import com.dcspa.prism.entity.Ministere;
import com.dcspa.prism.entity.Personnemorale;
import com.dcspa.prism.repository.PersonnemoraleRepository;
import com.dcspa.prism.service.MinistereService;
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

@RestController
@RequestMapping("/api/ministeres")
@RequiredArgsConstructor
public class MinistereController {

	private final MinistereService ministereService;
	private final PersonnemoraleRepository personnemoraleRepository;

	@GetMapping
	public ResponseEntity<List<Ministere>> findAll() {
		List<Ministere> list = ministereService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Ministere> findById(@PathVariable Integer id) {
		return ministereService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Ministere> create(@RequestBody MinistereRequest request) {
		Ministere saved = ministereService.save(toEntity(null, request));
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Ministere> update(@PathVariable Integer id, @RequestBody MinistereRequest request) {
		Ministere saved = ministereService.save(toEntity(id, request));
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		ministereService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Ministere toEntity(Integer id, MinistereRequest r) {
		if (r.getIdPromoteur() == null) {
			throw new IllegalArgumentException("idPromoteur est obligatoire.");
		}
		Personnemorale pm = personnemoraleRepository.findById(r.getIdPromoteur())
				.orElseThrow(() -> new IllegalArgumentException("Personne morale introuvable: " + r.getIdPromoteur()));
		Ministere m = new Ministere();
		if (id != null) {
			m.setId(id);
		}
		m.setPersonnemorale(pm);
		m.setLibelleMinistere(r.getLibelleMinistere());
		String codePromoteur = r.getCodePromoteur();
		if (id != null) {
			codePromoteur = ministereService.findById(id)
					.map(ex -> AutoCodePutMerge.mergeCodeString(r.getCodePromoteur(), ex.getCodePromoteur()))
					.orElse(codePromoteur);
		}
		m.setCodePromoteur(codePromoteur);
		m.setLibellePromoteur(r.getLibellePromoteur());
		m.setDenomination(r.getDenomination());
		m.setNomProgramme(r.getNomProgramme());
		m.setNomRepresentantLegalStructure(r.getNomRepresentantLegalStructure());
		m.setContact(r.getContact());
		m.setBoitePostale(r.getBoitePostale());
		m.setMail(r.getMail());
		return m;
	}
}
