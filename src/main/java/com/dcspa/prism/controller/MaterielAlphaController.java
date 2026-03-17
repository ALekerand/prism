package com.dcspa.prism.controller;

import com.dcspa.prism.dto.MaterielAlphaRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.MaterielAlpha;
import com.dcspa.prism.entity.MaterielsPedagogique;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.MaterielsPedagogiqueRepository;
import com.dcspa.prism.service.MaterielAlphaService;

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
@RequestMapping("/api/materielalpha")
@RequiredArgsConstructor
public class MaterielAlphaController {

	private final MaterielAlphaService materielalphaService;
	private final AlphaRepository alphaRepository;
	private final MaterielsPedagogiqueRepository materielsPedagogiqueRepository;

	@GetMapping
	public ResponseEntity<List<MaterielAlpha>> findAll() {
		List<MaterielAlpha> list = materielalphaService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MaterielAlpha> findById(@PathVariable Integer id) {
		return materielalphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<MaterielAlpha> create(@RequestBody MaterielAlphaRequest request) {
		MaterielAlpha saved = materielalphaService.save(toEntity(null, request));
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<MaterielAlpha> update(@PathVariable Integer id, @RequestBody MaterielAlphaRequest request) {
		MaterielAlpha saved = materielalphaService.save(toEntity(id, request));
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		materielalphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private MaterielAlpha toEntity(Integer id, MaterielAlphaRequest r) {
		MaterielAlpha m = new MaterielAlpha();
		m.setId(id);
		Alpha centre = alphaRepository.findById(r.getIdCentre())
				.orElseThrow(() -> new IllegalArgumentException("Alpha introuvable: " + r.getIdCentre()));
		MaterielsPedagogique mp = materielsPedagogiqueRepository.findById(r.getIdMaterielPedagogique())
				.orElseThrow(() -> new IllegalArgumentException("MaterielPedagogique introuvable: " + r.getIdMaterielPedagogique()));
		m.setIdCentre(centre);
		m.setIdMaterielPedagogique(mp);
		m.setLibelleAutreMateriel(r.getLibelleAutreMateriel());
		return m;
	}
}
