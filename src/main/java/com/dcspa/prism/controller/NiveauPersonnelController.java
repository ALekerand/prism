package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.NiveauPersonnel;
import com.dcspa.prism.service.NiveauPersonnelService;
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
@RequestMapping("/api/niveau-personnel")
@RequiredArgsConstructor
public class NiveauPersonnelController {

	private final NiveauPersonnelService niveauPersonnelService;

	@GetMapping
	public ResponseEntity<List<NiveauPersonnel>> findAll() {
		return ResponseEntity.ok(niveauPersonnelService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<NiveauPersonnel> findById(@PathVariable Integer id) {
		return niveauPersonnelService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<NiveauPersonnel> create(@RequestBody NiveauPersonnel body) {
		return ResponseEntity.status(201).body(niveauPersonnelService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<NiveauPersonnel> update(@PathVariable Integer id, @RequestBody NiveauPersonnel body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, niveauPersonnelService::findById, niveauPersonnelService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		niveauPersonnelService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
