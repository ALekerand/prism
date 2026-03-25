package com.dcspa.prism.controller;

import com.dcspa.prism.entity.SousPrefecture;
import com.dcspa.prism.service.SousPrefectureService;
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
@RequestMapping("/api/sous-prefecture")
@RequiredArgsConstructor
public class SousPrefectureController {

	private final SousPrefectureService sousPrefectureService;

	@GetMapping
	public ResponseEntity<List<SousPrefecture>> findAll() {
		return ResponseEntity.ok(sousPrefectureService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SousPrefecture> findById(@PathVariable Integer id) {
		return sousPrefectureService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<SousPrefecture> create(@RequestBody SousPrefecture body) {
		return ResponseEntity.status(201).body(sousPrefectureService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<SousPrefecture> update(@PathVariable Integer id, @RequestBody SousPrefecture body) {
		body.setId(id);
		return ResponseEntity.ok(sousPrefectureService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		sousPrefectureService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
