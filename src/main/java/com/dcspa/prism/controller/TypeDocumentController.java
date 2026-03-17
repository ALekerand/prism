package com.dcspa.prism.controller;

import com.dcspa.prism.entity.TypeDocument;
import com.dcspa.prism.service.TypeDocumentService;
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
@RequestMapping("/api/v1/TypeDocuments")
@RequiredArgsConstructor
public class TypeDocumentController {

	private final TypeDocumentService TypeDocumentService;

	@GetMapping
	public ResponseEntity<List<TypeDocument>> findAll() {
		List<TypeDocument> list = TypeDocumentService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TypeDocument> findById(@PathVariable Integer id) {
		return TypeDocumentService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<TypeDocument> create(@RequestBody TypeDocument TypeDocument) {
		TypeDocument saved = TypeDocumentService.save(TypeDocument);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TypeDocument> update(@PathVariable Integer id, @RequestBody TypeDocument TypeDocument) {
		TypeDocument.setId(id);
		TypeDocument saved = TypeDocumentService.save(TypeDocument);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		TypeDocumentService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
