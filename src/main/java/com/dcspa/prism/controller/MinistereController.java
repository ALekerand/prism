package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Ministere;
import com.dcspa.prism.service.MinistereService;


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
@RequestMapping("/api/ministeres")
@RequiredArgsConstructor
public class MinistereController {

	private final MinistereService ministereService;

	@GetMapping
	public ResponseEntity<List<Ministere>> findAll() {
		List<Ministere> list = ministereService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Ministere> findById(@PathVariable Integer id) {
		return ministereService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Ministere> create(@RequestBody Ministere ministere) {
		Ministere saved = ministereService.save(ministere);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Ministere> update(@PathVariable Integer id, @RequestBody Ministere ministere) {
		ministere.setId(id);
		Ministere saved = ministereService.save(ministere);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		ministereService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
