package com.dcspa.prism.controller;

import com.dcspa.prism.entity.StructureFormationCertification;
import com.dcspa.prism.service.StructureFormationCertificationService;
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
@RequestMapping("/api/structure-formation-certification")
@RequiredArgsConstructor
public class StructureFormationCertificationController {

	private final StructureFormationCertificationService structureFormationCertificationService;

	@GetMapping
	public ResponseEntity<List<StructureFormationCertification>> findAll() {
		return ResponseEntity.ok(structureFormationCertificationService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<StructureFormationCertification> findById(@PathVariable Integer id) {
		return structureFormationCertificationService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<StructureFormationCertification> create(@RequestBody StructureFormationCertification body) {
		return ResponseEntity.status(201).body(structureFormationCertificationService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<StructureFormationCertification> update(@PathVariable Integer id, @RequestBody StructureFormationCertification body) {
		body.setId(id);
		return ResponseEntity.ok(structureFormationCertificationService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		structureFormationCertificationService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
