package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.SuiviIeppRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.SuiviIepp;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.SuiviIeppRepository;
import com.dcspa.prism.security.AuthUser;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/suivi-iepp")
@RequiredArgsConstructor
public class SuiviIeppController {
	private final SuiviIeppRepository repository;
	private final AlphaRepository alphaRepository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<?> findAll(@AuthenticationPrincipal AuthUser user) {
		if (canReadIepp(user)) {
			return ResponseEntity.ok(repository.findAll().stream().map(this::toRow).toList());
		}
		if (canReadCentrale(user)) {
			return ResponseEntity.ok(repository.findAll().stream()
					.filter(e -> Boolean.TRUE.equals(e.getValideeIepp()))
					.map(this::toRow).toList());
		}
		return forbidden("SUIVI_IEPP:LIRE");
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		Optional<SuiviIepp> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		SuiviIepp e = opt.get();
		if (canReadIepp(user) || (canReadCentrale(user) && Boolean.TRUE.equals(e.getValideeIepp()))) {
			return ResponseEntity.ok(toRow(e));
		}
		return forbidden("SUIVI_IEPP:LIRE");
	}

	@Transactional
	@PostMapping
	public ResponseEntity<?> create(@RequestBody SuiviIeppRequest body, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = require(user, "CREER");
		if (denied != null) return denied;
		try {
			SuiviIepp e = new SuiviIepp();
			apply(e, body);
			e.setValideeIepp(false);
			return ResponseEntity.status(201).body(toRow(repository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody SuiviIeppRequest body, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = require(user, "MODIFIER");
		if (denied != null) return denied;
		Optional<SuiviIepp> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		SuiviIepp e = opt.get();
		if (Boolean.TRUE.equals(e.getValideeIepp())) {
			return ResponseEntity.badRequest().body(Map.of("message", "Modification impossible : le suivi IEPP est déjà validé."));
		}
		try {
			apply(e, body);
			return ResponseEntity.ok(toRow(repository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@Transactional
	@PutMapping("/{id}/valider")
	public ResponseEntity<?> validate(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = require(user, "VALIDER");
		if (denied != null) return denied;
		Optional<SuiviIepp> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		SuiviIepp e = opt.get();
		e.setValideeIepp(true);
		return ResponseEntity.ok(toRow(repository.save(e)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = require(user, "MODIFIER");
		if (denied != null) return denied;
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private ResponseEntity<?> require(AuthUser user, String permission) {
		if (user != null && user.hasPermission("SUIVI_IEPP", permission)) {
			return null;
		}
		return forbidden("SUIVI_IEPP:" + permission);
	}

	private boolean canReadIepp(AuthUser user) {
		return user != null && user.hasPermission("SUIVI_IEPP", "LIRE");
	}

	private boolean canReadCentrale(AuthUser user) {
		return user != null && user.hasPermission("SUIVI_CENTRALE", "LIRE");
	}

	private ResponseEntity<?> forbidden(String permission) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(Map.of("message", "Accès refusé: permission " + permission + " requise"));
	}

	private void apply(SuiviIepp e, SuiviIeppRequest r) {
		if (r == null || r.getIdAlpha() == null) throw new IllegalArgumentException("idAlpha est obligatoire");
		Alpha alpha = alphaRepository.findById(r.getIdAlpha()).orElseThrow(() -> new IllegalArgumentException("Alpha introuvable: " + r.getIdAlpha()));
		e.setIdAlpha(alpha);
		e.setNombreVisiteEffectueParIepp(r.getNombreVisiteEffectueParIepp());
		e.setNombreReunionPointActiviteAlpha(r.getNombreReunionPointActiviteAlpha());
	}

	private Map<String, Object> toRow(SuiviIepp e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "Alpha", e.getIdAlpha());
		m.put("nombreVisiteEffectueParIepp", e.getNombreVisiteEffectueParIepp());
		m.put("nombreReunionPointActiviteAlpha", e.getNombreReunionPointActiviteAlpha());
		m.put("valideeIepp", Boolean.TRUE.equals(e.getValideeIepp()));
		return m;
	}
}
