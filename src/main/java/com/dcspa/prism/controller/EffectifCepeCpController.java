package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.EffectifSaisieWorkflowSupport;
import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.EffectifCepeCp;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.EffectifCepeCpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/effectif-cepe-cp")
@RequiredArgsConstructor
public class EffectifCepeCpController {

	private static final String WORKFLOW_RESOURCE = "/api/effectif-cepe-cp";

	private final EffectifCepeCpService effectifCepeCpService;
	private final EffectifSaisieWorkflowSupport effectifWorkflow;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll(@AuthenticationPrincipal AuthUser user) {
		List<Map<String, Object>> rows = effectifCepeCpService.findAll().stream()
				.map(this::toRow)
				.collect(Collectors.toList());
		return ResponseEntity.ok(effectifWorkflow.enrichAndFilterList(WORKFLOW_RESOURCE, rows, user));
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(
			@PathVariable Integer id,
			@AuthenticationPrincipal AuthUser user) {
		return effectifCepeCpService.findById(id)
				.map(this::toRow)
				.flatMap(row -> effectifWorkflow.enrichAndFilterOne(WORKFLOW_RESOURCE, row, user))
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(
			@RequestBody EffectifCepeCp body,
			@AuthenticationPrincipal AuthUser user) {
		EffectifCepeCp saved = effectifCepeCpService.save(body);
		effectifWorkflow.claimAfterCreate(WORKFLOW_RESOURCE, saved.getId(), user);
		Map<String, Object> row = toRow(saved);
		effectifWorkflow.enrichRows(WORKFLOW_RESOURCE, List.of(row), user);
		return ResponseEntity.status(201).body(row);
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody EffectifCepeCp body) {
		Optional<EffectifCepeCp> opt = effectifCepeCpService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(effectifCepeCpService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		effectifWorkflow.purgeOnDelete(WORKFLOW_RESOURCE, id);
		effectifCepeCpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(EffectifCepeCp e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "AnneeScolaire", e.getIdAnneeScolaire());
		ReferentielEnricher.putRef(m, "PeriodeActivite", e.getIdPeriodeActivite());
		ReferentielEnricher.putRef(m, "Centre", e.getIdCentre());
		m.put("codeEffectifCepeCp", e.getCodeEffectifCepeCp());
		m.put("effectifCepeCpNiveauH", e.getEffectifCepeCpNiveauH());
		m.put("effectifCepeCpNiveauF", e.getEffectifCepeCpNiveauF());
		m.put("effectifCepeCandidatFCp", e.getEffectifCepeCandidatFCp());
		m.put("effectifCepeCandidatHCp", e.getEffectifCepeCandidatHCp());
		m.put("effectifCepeCandidatIvoirienCp", e.getEffectifCepeCandidatIvoirienCp());
		m.put("effectifCepeCandidatHandicapFCp", e.getEffectifCepeCandidatHandicapFCp());
		m.put("effectifCepeCandidatHandicapHCp", e.getEffectifCepeCandidatHandicapHCp());
		m.put("effectifCepeAdmisFCp", e.getEffectifCepeAdmisFCp());
		m.put("effectifCepeAdmisHCp", e.getEffectifCepeAdmisHCp());
		m.put("effectifCepeAdmisIvoirienCp", e.getEffectifCepeAdmisIvoirienCp());
		m.put("effectifCepeAdmisHandicapFCp", e.getEffectifCepeAdmisHandicapFCp());
		m.put("effectifCepeAdmisHandicapHCp", e.getEffectifCepeAdmisHandicapHCp());
		return m;
	}
}
