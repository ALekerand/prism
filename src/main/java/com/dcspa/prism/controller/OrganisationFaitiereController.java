package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;
import com.dcspa.prism.entity.OrganisationFaitiere;
import com.dcspa.prism.service.OrganisationFaitiereService;
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
@RequestMapping("/api/organisation-faitiere")
@RequiredArgsConstructor
public class OrganisationFaitiereController {

	private final OrganisationFaitiereService organisationFaitiereService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(organisationFaitiereService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return organisationFaitiereService.findById(id).map(this::toRow).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody OrganisationFaitiere body) {
		return ResponseEntity.status(201).body(toRow(organisationFaitiereService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody OrganisationFaitiere body) {
		Optional<OrganisationFaitiere> opt = organisationFaitiereService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(organisationFaitiereService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		organisationFaitiereService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(OrganisationFaitiere e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		m.put("codeOrganisationFaitiere", e.getCodeOrganisationFaitiere());
		m.put("libelleOrganisationFaitiere", e.getLibelleOrganisationFaitiere());
		m.put("sigleOrganisationFaitiere", e.getSigleOrganisationFaitiere());
		m.put("pointFocal", e.getPointFocal());
		m.put("fonctionPointFocal", e.getFonctionPointFocal());
		m.put("contacts", e.getContacts());
		m.put("courriel", e.getCourriel());
		m.put("code", e.getSigleOrganisationFaitiere() != null ? e.getSigleOrganisationFaitiere() : e.getCodeOrganisationFaitiere());
		m.put("libelle", e.getLibelleOrganisationFaitiere());
		return m;
	}
}
