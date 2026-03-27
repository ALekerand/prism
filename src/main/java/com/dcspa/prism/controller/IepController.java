package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Iep;
import com.dcspa.prism.service.IepService;
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
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/iep")
@RequiredArgsConstructor
public class IepController {

	private final IepService iepService;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		// Evite LazyInitializationException sur drena
		List<Map<String, Object>> list = iepService.findAll().stream()
				.map(i -> Map.<String, Object>of(
						"id", i.getId(),
						"codeIep", i.getCodeIep(),
						"nomIep", i.getNomIep()
				))
				.collect(Collectors.toList());
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Iep> findById(@PathVariable Integer id) {
		return iepService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Iep> create(@RequestBody Iep body) {
		return ResponseEntity.status(201).body(iepService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Iep> update(@PathVariable Integer id, @RequestBody Iep body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, iepService::findById, iepService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		iepService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
