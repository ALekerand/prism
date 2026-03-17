package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Modealphabetisation;
import com.dcspa.prism.service.ModealphabetisationService;
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
@RequestMapping("/api/modealpha")
@RequiredArgsConstructor
public class ModeAlphaController {

	private final ModealphabetisationService modealphaService;

	@GetMapping
	public ResponseEntity<List<Modealphabetisation>> findAll() {
		List<Modealphabetisation> list = modealphaService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Modealphabetisation> findById(@PathVariable Integer id) {
		return modealphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Modealphabetisation> create(@RequestBody Modealphabetisation modealpha) {
		Modealphabetisation saved = modealphaService.save(modealpha);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Modealphabetisation> update(@PathVariable Integer id, @RequestBody Modealphabetisation modealpha) {
		modealpha.setId(id);
		Modealphabetisation saved = modealphaService.save(modealpha);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		modealphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
