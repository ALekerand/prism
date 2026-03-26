package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Cec;
import com.dcspa.prism.service.CecService;
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
@RequestMapping("/api/cec")
@RequiredArgsConstructor
public class CecController {

	private final CecService cecService;

	@GetMapping
	public ResponseEntity<List<Cec>> findAll() {
		return ResponseEntity.ok(cecService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cec> findById(@PathVariable Integer id) {
		return cecService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Cec> create(@RequestBody Cec body) {
		return ResponseEntity.status(201).body(cecService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cec> update(@PathVariable Integer id, @RequestBody Cec body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, cecService::findById, cecService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		cecService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
