package com.dcspa.prism.controller;

import com.dcspa.prism.entity.NiveauCp;
import com.dcspa.prism.service.NiveauCpService;

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
@RequestMapping("/api/niveaucp")
@RequiredArgsConstructor
public class NiveauCpController {

	private final NiveauCpService niveaucpService;

	@GetMapping
	public ResponseEntity<List<NiveauCp>> findAll() {
		List<NiveauCp> list = niveaucpService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<NiveauCp> findById(@PathVariable Integer id) {
		return niveaucpService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<NiveauCp> create(@RequestBody NiveauCp niveaucp) {
		NiveauCp saved = niveaucpService.save(niveaucp);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<NiveauCp> update(@PathVariable Integer id, @RequestBody NiveauCp niveaucp) {
		niveaucp.setId(id);
		NiveauCp saved = niveaucpService.save(niveaucp);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		niveaucpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
