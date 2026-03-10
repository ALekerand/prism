package com.dcspa.prism.controller;

import com.dcspa.prism.dto.AlphaDto;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.service.AlphaService;
import com.dcspa.prism.service.mapper.AlphaMapper;
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
	private final AlphaMapper alphaMapper;

	@GetMapping
	public ResponseEntity<List<AlphaDto>> findAll() {
		List<Alpha> list = alphaService.findAll();
		List<AlphaDto> dtos = list.stream().map(alphaMapper::toDto).toList();
		return ResponseEntity.ok(dtos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AlphaDto> findById(@PathVariable Integer id) {
		return alphaService.findById(id)
				.map(alphaMapper::toDto)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
