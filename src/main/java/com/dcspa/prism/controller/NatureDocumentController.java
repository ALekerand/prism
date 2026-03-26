package com.dcspa.prism.controller;
import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.NatureDocument;
import com.dcspa.prism.service.NatureDocumentService;

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
@RequestMapping("/api/naturedocument")
@RequiredArgsConstructor
public class NatureDocumentController {

	private final NatureDocumentService naturedocumentService;

	@GetMapping
	public ResponseEntity<List<NatureDocument>> findAll() {
		List<NatureDocument> list = naturedocumentService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<NatureDocument> findById(@PathVariable Integer id) {
		return naturedocumentService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<NatureDocument> create(@RequestBody NatureDocument naturedocument) {
		NatureDocument saved = naturedocumentService.save(naturedocument);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<NatureDocument> update(@PathVariable Integer id, @RequestBody NatureDocument naturedocument) {
		return ReferentialPutHelper.putPreservingAutoCode(id, naturedocument, naturedocumentService::findById, naturedocumentService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		naturedocumentService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
