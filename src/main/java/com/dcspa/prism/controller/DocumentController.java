package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.DocumentRequest;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Document;
import com.dcspa.prism.entity.NatureDocument;
import com.dcspa.prism.entity.TypeDocument;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.NatureDocumentRepository;
import com.dcspa.prism.repository.TypeDocumentRepository;
import com.dcspa.prism.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

	private final DocumentService documentService;
	private final NatureDocumentRepository natureDocumentRepository;
	private final TypeDocumentRepository typeDocumentRepository;
	private final CentreRepository centreRepository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(documentService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return documentService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody DocumentRequest request) {
		return ResponseEntity.status(201).body(toRow(documentService.save(toEntity(null, request))));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody DocumentRequest request) {
		if (documentService.findById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(toRow(documentService.save(toEntity(id, request))));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		documentService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(Document d) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", d.getId());
		ReferentielEnricher.putRef(m, "NatureDocument", d.getIdNatureDocument());
		ReferentielEnricher.putRef(m, "TypeDocument", d.getIdTypeDocument());
		ReferentielEnricher.putRef(m, "Centre", d.getIdCentre());
		m.put("existe", d.getExiste());
		m.put("ajour", d.getAjour());
		m.put("bientenu", d.getBientenu());
		m.put("respmethode", d.getRespmethode());
		m.put("bienrensigne", d.getBienrensigne());
		m.put("codeDocument", d.getCodeDocument());
		return m;
	}

	private Document toEntity(Integer id, DocumentRequest r) {
		Document d = new Document();
		d.setId(id);

		NatureDocument nature = natureDocumentRepository.findById(toLong(r.getIdNatureDocument()))
				.orElseThrow(() -> new IllegalArgumentException("NatureDocument introuvable: " + r.getIdNatureDocument()));
		TypeDocument type = typeDocumentRepository.findById(toLong(r.getIdTypeDocument()))
				.orElseThrow(() -> new IllegalArgumentException("TypeDocument introuvable: " + r.getIdTypeDocument()));
		Centre centre = centreRepository.findById(r.getIdCentre())
				.orElseThrow(() -> new IllegalArgumentException("Centre introuvable: " + r.getIdCentre()));

		d.setIdNatureDocument(nature);
		d.setIdTypeDocument(type);
		d.setIdCentre(centre);

		d.setExiste(r.getExiste());
		d.setAjour(r.getAjour());
		d.setBientenu(r.getBientenu());
		d.setRespmethode(r.getRespmethode());
		d.setBienrensigne(r.getBienrensigne());
		String codeDocument = r.getCodeDocument();
		if (id != null) {
			codeDocument = documentService.findById(id)
					.map(ex -> AutoCodePutMerge.mergeCodeString(r.getCodeDocument(), ex.getCodeDocument()))
					.orElse(codeDocument);
		}
		d.setCodeDocument(codeDocument);
		return d;
	}

	private static Long toLong(Integer v) {
		if (v == null) {
			return null;
		}
		return v.longValue();
	}
}
