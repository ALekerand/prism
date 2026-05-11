package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.Personnel;
import com.dcspa.prism.service.PersonnelService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/personnel")
@RequiredArgsConstructor
public class PersonnelController {

	private final PersonnelService personnelService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(personnelService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return personnelService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody Personnel body) {
		return ResponseEntity.status(201).body(toRow(personnelService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody Personnel body) {
		Optional<Personnel> opt = personnelService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(personnelService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		personnelService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(Personnel e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "NiveauPersonnel", e.getIdNiveauPersonnel());
		ReferentielEnricher.putRef(m, "Fonction", e.getIdFonction());
		ReferentielEnricher.putRef(m, "Civilite", e.getIdCivilite());
		ReferentielEnricher.putRef(m, "Centre", e.getIdCentre());
		ReferentielEnricher.putRef(m, "StructureFormationCertification", e.getIdStructureFormationCertification());
		ReferentielEnricher.putRef(m, "StatutPersonnel", e.getIdStatutPersonnel());
		m.put("codePersonnel", e.getCodePersonnel());
		m.put("certifierPersonnel", e.getCertifierPersonnel());
		m.put("nomPersonnel", e.getNomPersonnel());
		m.put("prenomsPersonnel", e.getPrenomsPersonnel());
		m.put("anneExpePersonnel", e.getAnneExpePersonnel());
		m.put("sexePersonnel", e.getSexePersonnel());
		m.put("dateNaissance", e.getDateNaissance());
		m.put("ancienneFonctPromoPesonnel", e.getAncienneFonctPromoPesonnel());
		m.put("contactPersonnel", e.getContactPersonnel());
		m.put("boitePostalePersonnel", e.getBoitePostalePersonnel());
		m.put("emailPersonnel", e.getEmailPersonnel());
		m.put("denominationPersonnel", e.getDenominationPersonnel());
		m.put("nomDuPrgramme", e.getNomDuPrgramme());
		m.put("nomRepresentantLegalSturcture", e.getNomRepresentantLegalSturcture());
		return m;
	}
}
