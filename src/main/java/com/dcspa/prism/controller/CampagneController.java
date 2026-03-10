package com.dcspa.prism.controller;

import com.dcspa.prism.dto.CampagneDto;
import com.dcspa.prism.entity.Campagne;
import com.dcspa.prism.service.CampagneService;
import com.dcspa.prism.service.mapper.CampagneMapper;
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
	private final CampagneMapper campagneMapper;

	@GetMapping
	public ResponseEntity<List<CampagneDto>> findAll() {
		List<Campagne> list = campagneService.findAll();
		List<CampagneDto> dtos = list.stream().map(campagneMapper::toDto).toList();
		return ResponseEntity.ok(dtos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CampagneDto> findById(@PathVariable Integer id) {
		return campagneService.findById(id)
				.map(campagneMapper::toDto)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
