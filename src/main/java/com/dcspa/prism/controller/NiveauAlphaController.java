package com.dcspa.prism.controller;

import com.dcspa.prism.entity.NiveauAlpha;
import com.dcspa.prism.service.NiveauAlphaService;

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
@RequestMapping("/api/niveaualpha")
@RequiredArgsConstructor
public class NiveauAlphaController {

	private final NiveauAlphaService niveaualphaService;

	@GetMapping
	public ResponseEntity<List<NiveauAlpha>> findAll() {
		List<NiveauAlpha> list = niveaualphaService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<NiveauAlpha> findById(@PathVariable Integer id) {
		return niveaualphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<NiveauAlpha> create(@RequestBody NiveauAlpha niveaualpha) {
		NiveauAlpha saved = niveaualphaService.save(niveaualpha);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<NiveauAlpha> update(@PathVariable Integer id, @RequestBody NiveauAlpha niveaualpha) {
		niveaualpha.setId(id);
		NiveauAlpha saved = niveaualphaService.save(niveaualpha);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		niveaualphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
