package com.dcspa.prism.controller;

import com.dcspa.prism.entity.NatureCentre;
import com.dcspa.prism.service.NatureCentreService;

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
@RequestMapping("/api/naturecentre")
@RequiredArgsConstructor
public class NatureCentreController {

	private final NatureCentreService naturecentreService;

	@GetMapping
	public ResponseEntity<List<NatureCentre>> findAll() {
		List<NatureCentre> list = naturecentreService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<NatureCentre> findById(@PathVariable Integer id) {
		return naturecentreService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<NatureCentre> create(@RequestBody NatureCentre naturecentre) {
		NatureCentre saved = naturecentreService.save(naturecentre);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<NatureCentre> update(@PathVariable Integer id, @RequestBody NatureCentre naturecentre) {
		naturecentre.setId(id);
		NatureCentre saved = naturecentreService.save(naturecentre);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		naturecentreService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
