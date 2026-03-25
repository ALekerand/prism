package com.dcspa.prism.controller;

import com.dcspa.prism.entity.SupportDidactiqueAlpha;
import com.dcspa.prism.service.SupportDidactiqueAlphaService;
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
@RequestMapping("/api/support-didactique-alpha")
@RequiredArgsConstructor
public class SupportDidactiqueAlphaController {

	private final SupportDidactiqueAlphaService supportDidactiqueAlphaService;

	@GetMapping
	public ResponseEntity<List<SupportDidactiqueAlpha>> findAll() {
		return ResponseEntity.ok(supportDidactiqueAlphaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SupportDidactiqueAlpha> findById(@PathVariable Integer id) {
		return supportDidactiqueAlphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<SupportDidactiqueAlpha> create(@RequestBody SupportDidactiqueAlpha body) {
		return ResponseEntity.status(201).body(supportDidactiqueAlphaService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<SupportDidactiqueAlpha> update(@PathVariable Integer id, @RequestBody SupportDidactiqueAlpha body) {
		body.setId(id);
		return ResponseEntity.ok(supportDidactiqueAlphaService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		supportDidactiqueAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
