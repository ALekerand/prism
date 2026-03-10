package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Campagne;
import com.dcspa.prism.service.CampagneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/campagnes")
@RequiredArgsConstructor
public class CampagneController {

	private final CampagneService campagneService;

	@GetMapping
	public ResponseEntity<List<Campagne>> findAll() {
		List<Campagne> list = campagneService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Campagne> findById(@PathVariable Integer id) {
		return campagneService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
