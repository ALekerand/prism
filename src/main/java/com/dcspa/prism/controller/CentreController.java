package com.dcspa.prism.controller;

import com.dcspa.prism.dto.CentreDto;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.service.CentreService;
import com.dcspa.prism.service.mapper.CentreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/centres")
@RequiredArgsConstructor
public class CentreController {

	private final CentreService centreService;
	private final CentreMapper centreMapper;

	@GetMapping
	public ResponseEntity<List<CentreDto>> findAll() {
		List<Centre> list = centreService.findAll();
		List<CentreDto> dtos = list.stream().map(centreMapper::toDto).toList();
		return ResponseEntity.ok(dtos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CentreDto> findById(@PathVariable Integer id) {
		return centreService.findById(id)
				.map(centreMapper::toDto)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
