package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;
import com.dcspa.prism.dto.NiveauAlphaRequest;
import com.dcspa.prism.entity.NiveauAlpha;
import com.dcspa.prism.service.NiveauAlphaService;

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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/niveaualpha")
@RequiredArgsConstructor
public class NiveauAlphaController {

	private final NiveauAlphaService niveaualphaService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		List<Map<String, Object>> list = niveaualphaService.findAll()
				.stream()
				.map(this::toRow)
				.collect(Collectors.toList());
		return ResponseEntity.ok(list);
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return niveaualphaService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody NiveauAlphaRequest request) {
		NiveauAlpha saved = niveaualphaService.save(toEntity(null, request));
		return ResponseEntity.status(200).body(toRow(saved));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody NiveauAlphaRequest request) {
		NiveauAlpha saved = niveaualphaService.save(toEntity(id, request));
		return ResponseEntity.ok(toRow(saved));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		niveaualphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private NiveauAlpha toEntity(Integer id, NiveauAlphaRequest r) {
		NiveauAlpha n = new NiveauAlpha();
		n.setId(id);
		String codeNiveauAlpha = r.getCodeNiveauAlpha();
		if (id != null) {
			codeNiveauAlpha = niveaualphaService.findById(id)
					.map(ex -> AutoCodePutMerge.mergeCodeString(r.getCodeNiveauAlpha(), ex.getCodeNiveauAlpha()))
					.orElse(codeNiveauAlpha);
		}
		n.setCodeNiveauAlpha(codeNiveauAlpha);
		n.setLibelleNiveauAlpha(r.getLibelleNiveauAlpha());
		return n;
	}

	private Map<String, Object> toRow(NiveauAlpha n) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", n.getId());
		m.put("codeNiveauAlpha", n.getCodeNiveauAlpha());
		m.put("libelleNiveauAlpha", n.getLibelleNiveauAlpha());
		return m;
	}
}
