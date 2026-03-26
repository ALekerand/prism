package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.CecNiveau;
import com.dcspa.prism.service.CecNiveauService;
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
@RequestMapping("/api/cec-niveau")
@RequiredArgsConstructor
public class CecNiveauController {

	private final CecNiveauService cecNiveauService;

	@GetMapping
	public ResponseEntity<List<CecNiveau>> findAll() {
		return ResponseEntity.ok(cecNiveauService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CecNiveau> findById(@PathVariable Integer id) {
		return cecNiveauService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<CecNiveau> create(@RequestBody CecNiveau body) {
		return ResponseEntity.status(201).body(cecNiveauService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CecNiveau> update(@PathVariable Integer id, @RequestBody CecNiveau body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, cecNiveauService::findById, cecNiveauService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		cecNiveauService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
