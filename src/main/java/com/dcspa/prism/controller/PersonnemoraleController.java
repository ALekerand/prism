package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Personnemorale;
import com.dcspa.prism.service.PersonnemoraleService;
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
@RequestMapping("/api/personnemorale")
@RequiredArgsConstructor
public class PersonnemoraleController {

	private final PersonnemoraleService personnemoraleService;

	@GetMapping
	public ResponseEntity<List<Personnemorale>> findAll() {
		return ResponseEntity.ok(personnemoraleService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Personnemorale> findById(@PathVariable Integer id) {
		return personnemoraleService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Personnemorale> create(@RequestBody Personnemorale body) {
		return ResponseEntity.status(201).body(personnemoraleService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Personnemorale> update(@PathVariable Integer id, @RequestBody Personnemorale body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, personnemoraleService::findById, personnemoraleService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		personnemoraleService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
