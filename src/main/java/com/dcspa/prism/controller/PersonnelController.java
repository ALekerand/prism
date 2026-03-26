package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.Personnel;
import com.dcspa.prism.service.PersonnelService;
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
@RequestMapping("/api/personnel")
@RequiredArgsConstructor
public class PersonnelController {

	private final PersonnelService personnelService;

	@GetMapping
	public ResponseEntity<List<Personnel>> findAll() {
		return ResponseEntity.ok(personnelService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Personnel> findById(@PathVariable Integer id) {
		return personnelService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Personnel> create(@RequestBody Personnel body) {
		return ResponseEntity.status(201).body(personnelService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Personnel> update(@PathVariable Integer id, @RequestBody Personnel body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, personnelService::findById, personnelService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		personnelService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
