package com.dcspa.prism.controller;

import com.dcspa.prism.entity.CategorieAppui;
import com.dcspa.prism.service.CategorieAppuiService;

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
@RequestMapping("/api/categorieappuis")
@RequiredArgsConstructor
public class CategorieAppuiController {

	private final CategorieAppuiService categorieappuiService;

	@GetMapping
	public ResponseEntity<List<CategorieAppui>> findAll() {
		List<CategorieAppui> list = categorieappuiService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategorieAppui> findById(@PathVariable Integer id) {
		return categorieappuiService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<CategorieAppui> create(@RequestBody CategorieAppui categorieappui) {
		CategorieAppui saved = categorieappuiService.save(categorieappui);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategorieAppui> update(@PathVariable Integer id, @RequestBody CategorieAppui categorieappui) {
		categorieappui.setId(id);
		CategorieAppui saved = categorieappuiService.save(categorieappui);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		categorieappuiService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
