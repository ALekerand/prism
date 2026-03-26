package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Ong;
import com.dcspa.prism.service.OngService;
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
@RequestMapping("/api/ong")
@RequiredArgsConstructor
public class OngController {

	private final OngService ongService;

	@GetMapping
	public ResponseEntity<List<Ong>> findAll() {
		return ResponseEntity.ok(ongService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Ong> findById(@PathVariable Integer id) {
		return ongService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Ong> create(@RequestBody Ong body) {
		return ResponseEntity.status(201).body(ongService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Ong> update(@PathVariable Integer id, @RequestBody Ong body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, ongService::findById, ongService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		ongService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
