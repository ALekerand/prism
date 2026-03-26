package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Sie;
import com.dcspa.prism.service.SieService;
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
@RequestMapping("/api/sie")
@RequiredArgsConstructor
public class SieController {

	private final SieService sieService;

	@GetMapping
	public ResponseEntity<List<Sie>> findAll() {
		return ResponseEntity.ok(sieService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Sie> findById(@PathVariable Integer id) {
		return sieService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Sie> create(@RequestBody Sie body) {
		return ResponseEntity.status(201).body(sieService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Sie> update(@PathVariable Integer id, @RequestBody Sie body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, sieService::findById, sieService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		sieService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
