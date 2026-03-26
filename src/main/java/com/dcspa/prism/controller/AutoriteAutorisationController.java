package com.dcspa.prism.controller;
import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.AutoriteAutorisation;
import com.dcspa.prism.service.AutoriteAutorisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autoriteautorisation")
@RequiredArgsConstructor
public class AutoriteAutorisationController {

	private final AutoriteAutorisationService autoriteAutorisationService;

	@GetMapping
	public ResponseEntity<List<AutoriteAutorisation>> findAll() {
		List<AutoriteAutorisation> list = autoriteAutorisationService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AutoriteAutorisation> findById(@PathVariable Integer id) {
		return autoriteAutorisationService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<AutoriteAutorisation> create(@RequestBody AutoriteAutorisation autoriteAutorisation) {
		AutoriteAutorisation saved = autoriteAutorisationService.save(autoriteAutorisation);
		return ResponseEntity.status(200).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AutoriteAutorisation> update(@PathVariable Integer id, @RequestBody AutoriteAutorisation autoriteAutorisation) {
		return ReferentialPutHelper.putPreservingAutoCode(id, autoriteAutorisation, autoriteAutorisationService::findById, autoriteAutorisationService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		autoriteAutorisationService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
