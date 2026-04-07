package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.entity.CompetenceCentre;
import com.dcspa.prism.service.CompetenceCentreService;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/competence-centre")
@RequiredArgsConstructor
public class CompetenceCentreController {

	private final CompetenceCentreService competenceCentreService;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(competenceCentreService.findAll().stream().map(this::toRow).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return competenceCentreService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody CompetenceCentre body) {
		CompetenceCentre saved = competenceCentreService.save(body);
		return ResponseEntity.status(201).body(toRow(saved));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody CompetenceCentre body) {
		Optional<CompetenceCentre> opt = competenceCentreService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(competenceCentreService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		competenceCentreService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(CompetenceCentre e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		m.put("idCompetence", JpaAssociationIds.intIdOrNull(e.getIdCompetence()));
		m.put("idCentre", JpaAssociationIds.intIdOrNull(e.getIdCentre()));
		m.put("codeCompetenceCentre", e.getCodeCompetenceCentre());
		return m;
	}
}
