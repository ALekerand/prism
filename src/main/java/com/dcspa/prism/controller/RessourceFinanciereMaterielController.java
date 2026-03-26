package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.RessourceFinanciereMateriel;
import com.dcspa.prism.service.RessourceFinanciereMaterielService;
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
@RequestMapping("/api/ressource-financiere-materiel")
@RequiredArgsConstructor
public class RessourceFinanciereMaterielController {

	private final RessourceFinanciereMaterielService ressourceFinanciereMaterielService;

	@GetMapping
	public ResponseEntity<List<RessourceFinanciereMateriel>> findAll() {
		return ResponseEntity.ok(ressourceFinanciereMaterielService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<RessourceFinanciereMateriel> findById(@PathVariable Integer id) {
		return ressourceFinanciereMaterielService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<RessourceFinanciereMateriel> create(@RequestBody RessourceFinanciereMateriel body) {
		return ResponseEntity.status(201).body(ressourceFinanciereMaterielService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<RessourceFinanciereMateriel> update(@PathVariable Integer id, @RequestBody RessourceFinanciereMateriel body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, ressourceFinanciereMaterielService::findById, ressourceFinanciereMaterielService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		ressourceFinanciereMaterielService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
