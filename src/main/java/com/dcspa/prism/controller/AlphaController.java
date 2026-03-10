package com.dcspa.prism.controller;

import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.service.AlphaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alpha")
@RequiredArgsConstructor
public class AlphaController {

	private final AlphaService alphaService;

	@GetMapping
	public ResponseEntity<List<Alpha>> findAll() {
		List<Alpha> list = alphaService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Alpha> findById(@PathVariable Integer id) {
		return alphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
