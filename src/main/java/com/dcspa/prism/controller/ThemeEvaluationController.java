package com.dcspa.prism.controller;

import com.dcspa.prism.codegen.AutoCodePutMerge;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.entity.ThemeEvaluation;
import com.dcspa.prism.repository.ThemeEvaluationRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/themes-evaluation")
@RequiredArgsConstructor
public class ThemeEvaluationController {
	private final ThemeEvaluationRepository repository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(repository.findAll().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return repository.findById(id)
				.map(this::toRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody ThemeEvaluation body) {
		return ResponseEntity.status(201).body(toRow(repository.save(body)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody ThemeEvaluation body) {
		Optional<ThemeEvaluation> opt = repository.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		body.setId(id);
		return ResponseEntity.ok(toRow(repository.save(body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Transactional(readOnly = true)
	@GetMapping("/search")
	public ResponseEntity<List<Map<String, Object>>> search(
			@RequestParam(required = false) String code,
			@RequestParam(required = false) String libelle,
			@RequestParam(required = false) String niveau) {
		String c = code == null ? null : code.toLowerCase();
		String l = libelle == null ? null : libelle.toLowerCase();
		String n = niveau == null ? null : niveau.trim();
		return ResponseEntity.ok(repository.findAll().stream()
				.filter(x -> c == null || (x.getCodeThemeEvaluation() != null
						&& x.getCodeThemeEvaluation().toLowerCase().contains(c)))
				.filter(x -> l == null || (x.getLibelleThemeEvaluation() != null
						&& x.getLibelleThemeEvaluation().toLowerCase().contains(l)))
				.filter(x -> n == null || n.equalsIgnoreCase(x.getNiveau()))
				.map(this::toRow)
				.toList());
	}

	private Map<String, Object> toRow(ThemeEvaluation t) {
		return new LinkedHashMap<>(ReferentielEnricher.toRef(t));
	}
}
