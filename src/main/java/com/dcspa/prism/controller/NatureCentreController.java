package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Naturecentre;
import com.dcspa.prism.service.NaturecentreService;

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

	private final NaturecentreService naturecentreService;

	@GetMapping
	public ResponseEntity<List<Naturecentre>> findAll() {
		List<Naturecentre> list = naturecentreService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Naturecentre> findById(@PathVariable Integer id) {
		return naturecentreService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Naturecentre> create(@RequestBody Naturecentre naturecentre) {
		Naturecentre saved = naturecentreService.save(naturecentre);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Naturecentre> update(@PathVariable Integer id, @RequestBody Naturecentre naturecentre) {
		return ReferentialPutHelper.putPreservingAutoCode(id, naturecentre, naturecentreService::findById, naturecentreService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		naturecentreService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
