package com.dcspa.prism.controller;
import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.MaterielsPedagogique;
import com.dcspa.prism.service.MaterielsPedagogiqueService;

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

	private final MaterielsPedagogiqueService materielpedagogiqueService;

	@GetMapping
	public ResponseEntity<List<MaterielsPedagogique>> findAll() {
		List<MaterielsPedagogique> list = materielpedagogiqueService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MaterielsPedagogique> findById(@PathVariable Integer id) {
		return materielpedagogiqueService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<MaterielsPedagogique> create(@RequestBody MaterielsPedagogique materielpedagogique) {
		MaterielsPedagogique saved = materielpedagogiqueService.save(materielpedagogique);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<MaterielsPedagogique> update(@PathVariable Integer id, @RequestBody MaterielsPedagogique materielpedagogique) {
		return ReferentialPutHelper.putPreservingAutoCode(id, materielpedagogique, materielpedagogiqueService::findById, materielpedagogiqueService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		materielpedagogiqueService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
