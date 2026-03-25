package com.dcspa.prism.controller;

import com.dcspa.prism.entity.ProgrammeAlpha;
import com.dcspa.prism.service.ProgrammeAlphaService;
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
@RequestMapping("/api/programme-alpha")
@RequiredArgsConstructor
public class ProgrammeAlphaController {

	private final ProgrammeAlphaService programmeAlphaService;

	@GetMapping
	public ResponseEntity<List<ProgrammeAlpha>> findAll() {
		return ResponseEntity.ok(programmeAlphaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProgrammeAlpha> findById(@PathVariable Integer id) {
		return programmeAlphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<ProgrammeAlpha> create(@RequestBody ProgrammeAlpha body) {
		return ResponseEntity.status(201).body(programmeAlphaService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProgrammeAlpha> update(@PathVariable Integer id, @RequestBody ProgrammeAlpha body) {
		body.setId(id);
		return ResponseEntity.ok(programmeAlphaService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		programmeAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
