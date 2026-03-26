package com.dcspa.prism.controller;
import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.AnneScolaire;
import com.dcspa.prism.service.AnneScolaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anneescolaire")
@RequiredArgsConstructor
public class AnneScolaireController {

	private final AnneScolaireService anneScolaireService;

	@GetMapping
	public ResponseEntity<List<AnneScolaire>> findAll() {
		List<AnneScolaire> list = anneScolaireService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AnneScolaire> findById(@PathVariable Integer id) {
		return anneScolaireService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<AnneScolaire> create(@RequestBody AnneScolaire anneScolaire) {
		AnneScolaire saved = anneScolaireService.save(anneScolaire);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AnneScolaire> update(@PathVariable Integer id, @RequestBody AnneScolaire anneScolaire) {
		return ReferentialPutHelper.putPreservingAutoCode(id, anneScolaire, anneScolaireService::findById, anneScolaireService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		anneScolaireService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
