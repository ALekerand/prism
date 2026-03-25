package com.dcspa.prism.controller;

import com.dcspa.prism.entity.AppuiPartenaire;
import com.dcspa.prism.service.AppuiPartenaireService;
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
@RequestMapping("/api/appui-partenaire")
@RequiredArgsConstructor
public class AppuiPartenaireController {

	private final AppuiPartenaireService appuiPartenaireService;

	@GetMapping
	public ResponseEntity<List<AppuiPartenaire>> findAll() {
		return ResponseEntity.ok(appuiPartenaireService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<AppuiPartenaire> findById(@PathVariable Integer id) {
		return appuiPartenaireService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<AppuiPartenaire> create(@RequestBody AppuiPartenaire body) {
		return ResponseEntity.status(201).body(appuiPartenaireService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<AppuiPartenaire> update(@PathVariable Integer id, @RequestBody AppuiPartenaire body) {
		body.setId(id);
		return ResponseEntity.ok(appuiPartenaireService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		appuiPartenaireService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
