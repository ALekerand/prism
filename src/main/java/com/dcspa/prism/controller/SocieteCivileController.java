package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.SocieteCivile;
import com.dcspa.prism.service.SocieteCivileService;
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
@RequestMapping("/api/societe-civile")
@RequiredArgsConstructor
public class SocieteCivileController {

	private final SocieteCivileService societeCivileService;

	@GetMapping
	public ResponseEntity<List<SocieteCivile>> findAll() {
		return ResponseEntity.ok(societeCivileService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SocieteCivile> findById(@PathVariable Integer id) {
		return societeCivileService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<SocieteCivile> create(@RequestBody SocieteCivile body) {
		return ResponseEntity.status(201).body(societeCivileService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<SocieteCivile> update(@PathVariable Integer id, @RequestBody SocieteCivile body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, societeCivileService::findById, societeCivileService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		societeCivileService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
