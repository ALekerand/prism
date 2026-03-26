package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

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

import java.util.List;

@RestController
@RequestMapping("/api/competence-centre")
@RequiredArgsConstructor
public class CompetenceCentreController {

	private final CompetenceCentreService competenceCentreService;

	@GetMapping
	public ResponseEntity<List<CompetenceCentre>> findAll() {
		return ResponseEntity.ok(competenceCentreService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CompetenceCentre> findById(@PathVariable Integer id) {
		return competenceCentreService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<CompetenceCentre> create(@RequestBody CompetenceCentre body) {
		return ResponseEntity.status(201).body(competenceCentreService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CompetenceCentre> update(@PathVariable Integer id, @RequestBody CompetenceCentre body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, competenceCentreService::findById, competenceCentreService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		competenceCentreService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
