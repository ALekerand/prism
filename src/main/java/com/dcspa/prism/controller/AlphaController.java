package com.dcspa.prism.controller;
import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.service.AlphaService;
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
@RequestMapping("/api/v1/alpha")
@RequiredArgsConstructor
public class AlphaController {

	private final AlphaService alphaService;

	@GetMapping
	public ResponseEntity<List<Alpha>> findAll() {
		List<Alpha> list = alphaService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Alpha> findById(@PathVariable Integer id) {
		return alphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Alpha> create(@RequestBody Alpha alpha) {
		Alpha saved = alphaService.save(alpha);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Alpha> update(@PathVariable Integer id, @RequestBody Alpha alpha) {
		return ReferentialPutHelper.putPreservingAutoCode(id, alpha, alphaService::findById, alphaService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		alphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
