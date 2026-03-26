package com.dcspa.prism.controller;
import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Competence;
import com.dcspa.prism.service.CompetenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competence")
@RequiredArgsConstructor
public class CompetenceController {

	private final CompetenceService competenceService;

	@GetMapping
	public ResponseEntity<List<Competence>> findAll() {
		List<Competence> list = competenceService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Competence> findById(@PathVariable Integer id) {
		return competenceService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Competence> create(@RequestBody Competence competence) {
		Competence saved = competenceService.save(competence);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Competence> update(@PathVariable Integer id, @RequestBody Competence competence) {
		return ReferentialPutHelper.putPreservingAutoCode(id, competence, competenceService::findById, competenceService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		competenceService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
