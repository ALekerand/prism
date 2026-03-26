package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;
import com.dcspa.prism.entity.Fonction;
import com.dcspa.prism.service.FonctionService;
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
@RequestMapping("/api/v1/fonctions")
@RequiredArgsConstructor
public class FonctionController {

	private final FonctionService fonctionService;

	@GetMapping
	public ResponseEntity<List<Fonction>> findAll() {
		List<Fonction> list = fonctionService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Fonction> findById(@PathVariable Integer id) {
		return fonctionService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Fonction> create(@RequestBody Fonction fonction) {
		Fonction saved = fonctionService.save(fonction);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Fonction> update(@PathVariable Integer id, @RequestBody Fonction fonction) {
		return ReferentialPutHelper.putPreservingAutoCode(id, fonction, fonctionService::findById, fonctionService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		fonctionService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
