package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Programme;
import com.dcspa.prism.service.ProgrammeService;
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
@RequestMapping("/api/programme")
@RequiredArgsConstructor
public class ProgrammeController {

	private final ProgrammeService programmeService;

	@GetMapping
	public ResponseEntity<List<Programme>> findAll() {
		return ResponseEntity.ok(programmeService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Programme> findById(@PathVariable Integer id) {
		return programmeService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Programme> create(@RequestBody Programme body) {
		return ResponseEntity.status(201).body(programmeService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Programme> update(@PathVariable Integer id, @RequestBody Programme body) {
		body.setId(id);
		return ResponseEntity.ok(programmeService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		programmeService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
