package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Personnephysique;
import com.dcspa.prism.service.PersonnephysiqueService;
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
@RequestMapping("/api/personnephysique")
@RequiredArgsConstructor
public class PersonnephysiqueController {

	private final PersonnephysiqueService personnephysiqueService;

	@GetMapping
	public ResponseEntity<List<Personnephysique>> findAll() {
		return ResponseEntity.ok(personnephysiqueService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Personnephysique> findById(@PathVariable Integer id) {
		return personnephysiqueService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Personnephysique> create(@RequestBody Personnephysique body) {
		return ResponseEntity.status(201).body(personnephysiqueService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Personnephysique> update(@PathVariable Integer id, @RequestBody Personnephysique body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, personnephysiqueService::findById, personnephysiqueService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		personnephysiqueService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
