package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Permission;
import com.dcspa.prism.service.PermissionService;
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
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

	private final PermissionService permissionService;

	@GetMapping
	public ResponseEntity<List<Permission>> findAll() {
		return ResponseEntity.ok(permissionService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Permission> findById(@PathVariable Integer id) {
		return permissionService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Permission> create(@RequestBody Permission body) {
		return ResponseEntity.status(201).body(permissionService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Permission> update(@PathVariable Integer id, @RequestBody Permission body) {
		body.setId(id);
		return ResponseEntity.ok(permissionService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		permissionService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
