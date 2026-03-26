package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.CpNiveau;
import com.dcspa.prism.service.CpNiveauService;
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
@RequestMapping("/api/cp-niveau")
@RequiredArgsConstructor
public class CpNiveauController {

	private final CpNiveauService cpNiveauService;

	@GetMapping
	public ResponseEntity<List<CpNiveau>> findAll() {
		return ResponseEntity.ok(cpNiveauService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CpNiveau> findById(@PathVariable Integer id) {
		return cpNiveauService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<CpNiveau> create(@RequestBody CpNiveau body) {
		return ResponseEntity.status(201).body(cpNiveauService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CpNiveau> update(@PathVariable Integer id, @RequestBody CpNiveau body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, cpNiveauService::findById, cpNiveauService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		cpNiveauService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
