package com.dcspa.prism.controller;

import com.dcspa.prism.entity.PeriodeActivite;
import com.dcspa.prism.service.PeriodeActiviteService;
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
@RequestMapping("/api/v1/PeriodeActivites")
@RequiredArgsConstructor
public class PeriodeActiviteController {

	private final PeriodeActiviteService PeriodeActiviteService;

	@GetMapping
	public ResponseEntity<List<PeriodeActivite>> findAll() {
		List<PeriodeActivite> list = PeriodeActiviteService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PeriodeActivite> findById(@PathVariable Integer id) {
		return PeriodeActiviteService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<PeriodeActivite> create(@RequestBody PeriodeActivite PeriodeActivite) {
		PeriodeActivite saved = PeriodeActiviteService.save(PeriodeActivite);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PeriodeActivite> update(@PathVariable Integer id, @RequestBody PeriodeActivite PeriodeActivite) {
		PeriodeActivite.setId(id);
		PeriodeActivite saved = PeriodeActiviteService.save(PeriodeActivite);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		PeriodeActiviteService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
