package com.dcspa.prism.controller;

import com.dcspa.prism.entity.RoleFonctionnalitePermission;
import com.dcspa.prism.service.RoleFonctionnalitePermissionService;
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
@RequestMapping("/api/role-fonctionnalite-permission")
@RequiredArgsConstructor
public class RoleFonctionnalitePermissionController {

	private final RoleFonctionnalitePermissionService roleFonctionnalitePermissionService;

	@GetMapping
	public ResponseEntity<List<RoleFonctionnalitePermission>> findAll() {
		return ResponseEntity.ok(roleFonctionnalitePermissionService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<RoleFonctionnalitePermission> findById(@PathVariable Integer id) {
		return roleFonctionnalitePermissionService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<RoleFonctionnalitePermission> create(@RequestBody RoleFonctionnalitePermission body) {
		return ResponseEntity.status(201).body(roleFonctionnalitePermissionService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<RoleFonctionnalitePermission> update(@PathVariable Integer id, @RequestBody RoleFonctionnalitePermission body) {
		body.setId(id);
		return ResponseEntity.ok(roleFonctionnalitePermissionService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		roleFonctionnalitePermissionService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
