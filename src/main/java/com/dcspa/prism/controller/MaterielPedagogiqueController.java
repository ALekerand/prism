package com.dcspa.prism.controller;

import com.dcspa.prism.entity.MaterielPedagogique;
import com.dcspa.prism.service.MaterielPedagogiqueService;

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
@RequestMapping("/api/materielpedagogiques")
@RequiredArgsConstructor
public class MaterielPedagogiqueController {

	private final MaterielPedagogiqueService materielpedagogiqueService;

	@GetMapping
	public ResponseEntity<List<MaterielPedagogique>> findAll() {
		List<MaterielPedagogique> list = materielpedagogiqueService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MaterielPedagogique> findById(@PathVariable Integer id) {
		return materielpedagogiqueService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<MaterielPedagogique> create(@RequestBody MaterielPedagogique materielpedagogique) {
		MaterielPedagogique saved = materielpedagogiqueService.save(materielpedagogique);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<MaterielPedagogique> update(@PathVariable Integer id, @RequestBody MaterielPedagogique materielpedagogique) {
		materielalpha.setId(id);
		MaterielPedagogique saved = materielpedagogiqueService.save(materielpedagogique);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		materielpedagogiqueService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
