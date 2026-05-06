package com.dcspa.prism.controller;
import com.dcspa.prism.controller.support.ReferentialPutHelper;

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
@RequestMapping("/api/Modealphabetisations")
@RequiredArgsConstructor
public class ModealphabetisationController {

	private final ModealphabetisationService ModealphabetisationService;

	@GetMapping
	public ResponseEntity<List<Modealphabetisation>> findAll() {
		List<Modealphabetisation> list = ModealphabetisationService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Modealphabetisation> findById(@PathVariable Integer id) {
		return ModealphabetisationService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Modealphabetisation> create(@RequestBody Modealphabetisation Modealphabetisation) {
		Modealphabetisation saved = ModealphabetisationService.save(Modealphabetisation);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Modealphabetisation> update(@PathVariable Integer id, @RequestBody Modealphabetisation Modealphabetisation) {
		return ReferentialPutHelper.putPreservingAutoCode(id, Modealphabetisation, ModealphabetisationService::findById, ModealphabetisationService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		ModealphabetisationService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}

