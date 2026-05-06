package com.dcspa.prism.controller;
import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Regimealphabetisation;
import com.dcspa.prism.service.RegimealphabetisationService;
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
@RequestMapping("/api/Regimealphabetisations")
@RequiredArgsConstructor
public class RegimealphabetisationController {

	private final RegimealphabetisationService RegimealphabetisationService;

	@GetMapping
	public ResponseEntity<List<Regimealphabetisation>> findAll() {
		List<Regimealphabetisation> list = RegimealphabetisationService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Regimealphabetisation> findById(@PathVariable Integer id) {
		return RegimealphabetisationService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Regimealphabetisation> create(@RequestBody Regimealphabetisation Regimealphabetisation) {
		Regimealphabetisation saved = RegimealphabetisationService.save(Regimealphabetisation);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Regimealphabetisation> update(@PathVariable Integer id, @RequestBody Regimealphabetisation Regimealphabetisation) {
		return ReferentialPutHelper.putPreservingAutoCode(id, Regimealphabetisation, RegimealphabetisationService::findById, RegimealphabetisationService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		RegimealphabetisationService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}

