package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;
import com.dcspa.prism.dto.NiveauAlphaRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.NiveauAlpha;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.service.NiveauAlphaService;

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
@RequestMapping("/api/niveaualpha")
@RequiredArgsConstructor
public class NiveauAlphaController {

	private final NiveauAlphaService niveaualphaService;
	private final AlphaRepository alphaRepository;

	@GetMapping
	public ResponseEntity<List<NiveauAlpha>> findAll() {
		List<NiveauAlpha> list = niveaualphaService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<NiveauAlpha> findById(@PathVariable Integer id) {
		return niveaualphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<NiveauAlpha> create(@RequestBody NiveauAlphaRequest request) {
		NiveauAlpha saved = niveaualphaService.save(toEntity(null, request));
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<NiveauAlpha> update(@PathVariable Integer id, @RequestBody NiveauAlphaRequest request) {
		NiveauAlpha saved = niveaualphaService.save(toEntity(id, request));
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		niveaualphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private NiveauAlpha toEntity(Integer id, NiveauAlphaRequest r) {
		NiveauAlpha n = new NiveauAlpha();
		n.setId(id);
		Alpha centre = alphaRepository.findById(r.getIdCentre())
				.orElseThrow(() -> new IllegalArgumentException("Alpha introuvable: " + r.getIdCentre()));
		n.setIdCentre(centre);
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
}
