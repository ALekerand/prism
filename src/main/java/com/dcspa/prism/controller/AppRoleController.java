package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.service.AppRoleService;
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
@RequestMapping("/api/app-role")
@RequiredArgsConstructor
public class AppRoleController {

	private final AppRoleService appRoleService;

	@GetMapping
	public ResponseEntity<List<AppRole>> findAll() {
		return ResponseEntity.ok(appRoleService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<AppRole> findById(@PathVariable Integer id) {
		return appRoleService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<AppRole> create(@RequestBody AppRole body) {
		return ResponseEntity.status(201).body(appRoleService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<AppRole> update(@PathVariable Integer id, @RequestBody AppRole body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, appRoleService::findById, appRoleService::save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		appRoleService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
