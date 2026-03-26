package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.LocaliteDImplantation;
import com.dcspa.prism.service.LocaliteDImplantationService;
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
@RequestMapping("/api/localite-d-implantation")
@RequiredArgsConstructor
public class LocaliteDImplantationController {

	private final LocaliteDImplantationService localiteDImplantationService;

	@GetMapping
	public ResponseEntity<List<LocaliteDImplantation>> findAll() {
		return ResponseEntity.ok(localiteDImplantationService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<LocaliteDImplantation> findById(@PathVariable Integer id) {
		return localiteDImplantationService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<LocaliteDImplantation> create(@RequestBody LocaliteDImplantation body) {
		return ResponseEntity.status(201).body(localiteDImplantationService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<LocaliteDImplantation> update(@PathVariable Integer id, @RequestBody LocaliteDImplantation body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, localiteDImplantationService::findById, localiteDImplantationService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		localiteDImplantationService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
