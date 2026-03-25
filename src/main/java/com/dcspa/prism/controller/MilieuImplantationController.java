package com.dcspa.prism.controller;

import com.dcspa.prism.entity.MilieuImplantation;
import com.dcspa.prism.service.MilieuImplantationService;
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
@RequestMapping("/api/milieu-implantation")
@RequiredArgsConstructor
public class MilieuImplantationController {

	private final MilieuImplantationService milieuImplantationService;

	@GetMapping
	public ResponseEntity<List<MilieuImplantation>> findAll() {
		return ResponseEntity.ok(milieuImplantationService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<MilieuImplantation> findById(@PathVariable Integer id) {
		return milieuImplantationService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<MilieuImplantation> create(@RequestBody MilieuImplantation body) {
		return ResponseEntity.status(201).body(milieuImplantationService.save(body));
	}

	@PutMapping("/{id}")
	public ResponseEntity<MilieuImplantation> update(@PathVariable Integer id, @RequestBody MilieuImplantation body) {
		body.setId(id);
		return ResponseEntity.ok(milieuImplantationService.save(body));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		milieuImplantationService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
