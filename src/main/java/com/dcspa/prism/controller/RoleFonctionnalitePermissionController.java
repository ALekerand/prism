package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.RoleFonctionnalitePermission;
import com.dcspa.prism.service.RoleFonctionnalitePermissionService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/role-fonctionnalite-permission")
@RequiredArgsConstructor
public class RoleFonctionnalitePermissionController {

	private final RoleFonctionnalitePermissionService roleFonctionnalitePermissionService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(roleFonctionnalitePermissionService.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return roleFonctionnalitePermissionService.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody RoleFonctionnalitePermission body) {
		return ResponseEntity.status(201).body(toRow(roleFonctionnalitePermissionService.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(
			@PathVariable Integer id, @RequestBody RoleFonctionnalitePermission body) {
		Optional<RoleFonctionnalitePermission> opt = roleFonctionnalitePermissionService.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		body.setId(id);
		return ResponseEntity.ok(toRow(roleFonctionnalitePermissionService.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		roleFonctionnalitePermissionService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toRow(RoleFonctionnalitePermission e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "AppRole", e.getRole());
		ReferentielEnricher.putRef(m, "Fonctionnalite", e.getFonctionnalite());
		ReferentielEnricher.putRef(m, "Permission", e.getPermission());
		return m;
	}
}
