package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.SourceFinancement;
import com.dcspa.prism.service.SourceFinancementService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

@RestController
@RequestMapping("/api/source-financement")
@RequiredArgsConstructor
public class SourceFinancementController {

	private final SourceFinancementService sourceFinancementService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(sourceFinancementService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return sourceFinancementService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody SourceFinancement body) {
		return ResponseEntity.status(201).body(toRow(sourceFinancementService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody SourceFinancement body) {
		Optional<SourceFinancement> opt = sourceFinancementService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(sourceFinancementService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		sourceFinancementService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(SourceFinancement e) {
		return new LinkedHashMap<>(ReferentielEnricher.toRef(e));
	}
}
