package com.dcspa.prism.controller;
import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.TypeAlpha;
import com.dcspa.prism.service.TypeAlphaService;
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
@RequestMapping("/api/TypeAlphas")
@RequiredArgsConstructor
public class TypeAlphaController {

	private final TypeAlphaService TypeAlphaService;

	@GetMapping
	public ResponseEntity<List<TypeAlpha>> findAll() {
		List<TypeAlpha> list = TypeAlphaService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TypeAlpha> findById(@PathVariable Integer id) {
		return TypeAlphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<TypeAlpha> create(@RequestBody TypeAlpha TypeAlpha) {
		TypeAlpha saved = TypeAlphaService.save(TypeAlpha);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TypeAlpha> update(@PathVariable Integer id, @RequestBody TypeAlpha TypeAlpha) {
		return ReferentialPutHelper.putPreservingAutoCode(id, TypeAlpha, TypeAlphaService::findById, TypeAlphaService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		TypeAlphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}

