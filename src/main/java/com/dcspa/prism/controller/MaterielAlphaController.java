package com.dcspa.prism.controller;

import com.dcspa.prism.entity.MaterielAlpha;
import com.dcspa.prism.service.MaterielAlphaService;

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
@RequestMapping("/api/materielalpha")
@RequiredArgsConstructor
public class MaterielAlphaController {

	private final MaterielAlphaService materielalphaService;

	@GetMapping
	public ResponseEntity<List<MaterielAlpha>> findAll() {
		List<MaterielAlpha> list = materielalphaService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MaterielAlpha> findById(@PathVariable Integer id) {
		return materielalphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<MaterielAlpha> create(@RequestBody MaterielAlpha materielalpha) {
		MaterielAlpha saved = materielalphaService.save(materielalpha);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<MaterielAlpha> update(@PathVariable Integer id, @RequestBody MaterielAlpha materielalpha) {
		materielalpha.setId(id);
		MaterielAlpha saved = materielalphaService.save(materielalpha);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		materielalphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
