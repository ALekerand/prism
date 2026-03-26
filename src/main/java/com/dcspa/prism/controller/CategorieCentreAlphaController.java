package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.CategorieCentreAlpha;
import com.dcspa.prism.service.CategorieCentreAlphaService;
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
@RequestMapping("/api/categorie-centre-alpha")
@RequiredArgsConstructor
public class CategorieCentreAlphaController {

	private final CategorieCentreAlphaService categorieCentreAlphaService;

	@GetMapping
	public ResponseEntity<List<CategorieCentreAlpha>> findAll() {
		return ResponseEntity.ok(categorieCentreAlphaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategorieCentreAlpha> findById(@PathVariable Integer id) {
		return categorieCentreAlphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<CategorieCentreAlpha> create(@RequestBody CategorieCentreAlpha body) {
		return ResponseEntity.status(201).body(categorieCentreAlphaService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategorieCentreAlpha> update(@PathVariable Integer id, @RequestBody CategorieCentreAlpha body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, categorieCentreAlphaService::findById, categorieCentreAlphaService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		categorieCentreAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
