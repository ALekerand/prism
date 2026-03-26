package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Infrastructure;
import com.dcspa.prism.service.InfrastructureService;
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
@RequestMapping("/api/infrastructure")
@RequiredArgsConstructor
public class InfrastructureController {

	private final InfrastructureService infrastructureService;

	@GetMapping
	public ResponseEntity<List<Infrastructure>> findAll() {
		return ResponseEntity.ok(infrastructureService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Infrastructure> findById(@PathVariable Integer id) {
		return infrastructureService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Infrastructure> create(@RequestBody Infrastructure body) {
		return ResponseEntity.status(201).body(infrastructureService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Infrastructure> update(@PathVariable Integer id, @RequestBody Infrastructure body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, infrastructureService::findById, infrastructureService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		infrastructureService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
