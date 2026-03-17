package com.dcspa.prism.controller;

import com.dcspa.prism.dto.DocumentRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.Document;
import com.dcspa.prism.entity.NatureDocument;
import com.dcspa.prism.entity.TypeDocument;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.NatureDocumentRepository;
import com.dcspa.prism.repository.TypeDocumentRepository;
import com.dcspa.prism.service.DocumentService;

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
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

	private final DocumentService documentService;
	private final NatureDocumentRepository natureDocumentRepository;
	private final TypeDocumentRepository typeDocumentRepository;
	private final AlphaRepository alphaRepository;

	@GetMapping
	public ResponseEntity<List<Document>> findAll() {
		List<Document> list = documentService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Document> findById(@PathVariable Integer id) {
		return documentService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Document> create(@RequestBody DocumentRequest request) {
		Document saved = documentService.save(toEntity(null, request));
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Document> update(@PathVariable Integer id, @RequestBody DocumentRequest request) {
		Document saved = documentService.save(toEntity(id, request));
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		documentService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Document toEntity(Integer id, DocumentRequest r) {
		Document d = new Document();
		d.setId(id);

		NatureDocument nature = natureDocumentRepository.findById(toLong(r.getIdNatureDocument()))
				.orElseThrow(() -> new IllegalArgumentException("NatureDocument introuvable: " + r.getIdNatureDocument()));
		TypeDocument type = typeDocumentRepository.findById(toLong(r.getIdTypeDocument()))
				.orElseThrow(() -> new IllegalArgumentException("TypeDocument introuvable: " + r.getIdTypeDocument()));
		Alpha centre = alphaRepository.findById(r.getIdCentre())
				.orElseThrow(() -> new IllegalArgumentException("Alpha introuvable: " + r.getIdCentre()));

		d.setIdNatureDocument(nature);
		d.setIdTypeDocument(type);
		d.setIdCentre(centre);

		d.setExiste(r.getExiste());
		d.setAjour(r.getAjour());
		d.setBientenu(r.getBientenu());
		d.setRespmethode(r.getRespmethode());
		d.setBienrensigne(r.getBienrensigne());
		d.setCodeDocument(r.getCodeDocument());
		return d;
	}

	private static Long toLong(Integer v) {
		if (v == null) return null;
		return v.longValue();
	}
}
