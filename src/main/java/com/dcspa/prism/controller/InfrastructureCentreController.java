package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.InfrastructureCentre;
import com.dcspa.prism.service.InfrastructureCentreService;
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
@RequestMapping("/api/infrastructure-centre")
@RequiredArgsConstructor
public class InfrastructureCentreController {

	private final InfrastructureCentreService infrastructureCentreService;

	@GetMapping
	public ResponseEntity<List<InfrastructureCentre>> findAll() {
		return ResponseEntity.ok(infrastructureCentreService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<InfrastructureCentre> findById(@PathVariable Integer id) {
		return infrastructureCentreService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<InfrastructureCentre> create(@RequestBody InfrastructureCentre body) {
		return ResponseEntity.status(201).body(infrastructureCentreService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<InfrastructureCentre> update(@PathVariable Integer id, @RequestBody InfrastructureCentre body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, infrastructureCentreService::findById, infrastructureCentreService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		infrastructureCentreService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
