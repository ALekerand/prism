package com.dcspa.prism.controller;
import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.SupportDidactique;
import com.dcspa.prism.service.SupportDidactiqueService;
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
@RequestMapping("/api/v1/SupportDidactiques")
@RequiredArgsConstructor
public class SupportDidactiqueController {

	private final SupportDidactiqueService SupportDidactiqueService;

	@GetMapping
	public ResponseEntity<List<SupportDidactique>> findAll() {
		List<SupportDidactique> list = SupportDidactiqueService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SupportDidactique> findById(@PathVariable Integer id) {
		return SupportDidactiqueService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<SupportDidactique> create(@RequestBody SupportDidactique SupportDidactique) {
		SupportDidactique saved = SupportDidactiqueService.save(SupportDidactique);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<SupportDidactique> update(@PathVariable Integer id, @RequestBody SupportDidactique SupportDidactique) {
		return ReferentialPutHelper.putPreservingAutoCode(id, SupportDidactique, SupportDidactiqueService::findById, SupportDidactiqueService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		SupportDidactiqueService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
