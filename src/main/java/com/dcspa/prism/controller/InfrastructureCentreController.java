package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.InfrastructureCentre;
import com.dcspa.prism.service.InfrastructureCentreService;
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
@RequestMapping("/api/infrastructure-centre")
@RequiredArgsConstructor
public class InfrastructureCentreController {

	private final InfrastructureCentreService infrastructureCentreService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(infrastructureCentreService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return infrastructureCentreService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody InfrastructureCentre body) {
		return ResponseEntity.status(201).body(toRow(infrastructureCentreService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(
			@PathVariable Integer id, @RequestBody InfrastructureCentre body) {
		Optional<InfrastructureCentre> opt = infrastructureCentreService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		body.setId(id);
		return ResponseEntity.ok(toRow(infrastructureCentreService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		infrastructureCentreService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(InfrastructureCentre e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "Centre", e.getIdCentre());
		ReferentielEnricher.putRef(m, "Infrastructure", e.getIdInfrastructure());
		m.put("libelleAutreInfrastructure", e.getLibelleAutreInfrastructure());
		return m;
	}
}
