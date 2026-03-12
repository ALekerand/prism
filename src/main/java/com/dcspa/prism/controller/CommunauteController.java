package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Communaute;
import com.dcspa.prism.service.CommunauteService;

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
@RequestMapping("/api/communautes")
@RequiredArgsConstructor
public class CommunauteController {

	private final CommunauteService communauteService;

	@GetMapping
	public ResponseEntity<List<Communaute>> findAll() {
		List<Communaute> list = communauteService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Communaute> findById(@PathVariable Integer id) {
		return communauteService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Communaute> create(@RequestBody Communaute communaute) {
		Communaute saved = communauteService.save(communaute);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Communaute> update(@PathVariable Integer id, @RequestBody Communaute communaute) {
		communaute.setId(id);
		Communaute saved = communauteService.save(communaute);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		communauteService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
