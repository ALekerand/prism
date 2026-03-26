package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Ptf;
import com.dcspa.prism.service.PtfService;
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
@RequestMapping("/api/ptf")
@RequiredArgsConstructor
public class PtfController {

	private final PtfService ptfService;

	@GetMapping
	public ResponseEntity<List<Ptf>> findAll() {
		return ResponseEntity.ok(ptfService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Ptf> findById(@PathVariable Integer id) {
		return ptfService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Ptf> create(@RequestBody Ptf body) {
		return ResponseEntity.status(201).body(ptfService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Ptf> update(@PathVariable Integer id, @RequestBody Ptf body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, ptfService::findById, ptfService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		ptfService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
