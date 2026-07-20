package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.PeriodeActivite;
import com.dcspa.prism.service.PeriodeActiviteService;
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
@RequestMapping("/api/PeriodeActivites")
@RequiredArgsConstructor
public class PeriodeActiviteController {

	private final PeriodeActiviteService PeriodeActiviteService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(PeriodeActiviteService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return PeriodeActiviteService.findById(id).map(this::toRow).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody PeriodeActivite body) {
		return ResponseEntity.status(201).body(toRow(PeriodeActiviteService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody PeriodeActivite body) {
		Optional<PeriodeActivite> opt = PeriodeActiviteService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(PeriodeActiviteService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		PeriodeActiviteService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(PeriodeActivite e) {
		Map<String, Object> row = new LinkedHashMap<>(ReferentielEnricher.toRef(e));
		row.put("dateDebut", e.getDateDebut());
		row.put("dateFin", e.getDateFin());
		row.put("horsDelai", com.dcspa.prism.service.PeriodeActiviteHorsDelai.isHorsDelai(e));
		return row;
	}
}
