package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.SuiviSuperviseurRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.PeriodeActivite;
import com.dcspa.prism.entity.SuiviSuperviseur;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.PeriodeActiviteRepository;
import com.dcspa.prism.repository.SuiviSuperviseurRepository;
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
@RequestMapping("/api/suivi-superviseur")
@RequiredArgsConstructor
public class SuiviSuperviseurController {
	private final SuiviSuperviseurRepository repository;
	private final AlphaRepository alphaRepository;
	private final PeriodeActiviteRepository periodeActiviteRepository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<?> findAll(@AuthenticationPrincipal AuthUser user) {
		if (canReadSuperviseur(user)) {
			return ResponseEntity.ok(repository.findAll().stream().map(this::toRow).toList());
		}
		if (canReadCentrale(user)) {
			return ResponseEntity.ok(repository.findAll().stream()
					.filter(e -> Boolean.TRUE.equals(e.getValideeSuperviseur()))
					.map(this::toRow).toList());
		}
		return forbidden("SUIVI_SUPERVISEUR:LIRE");
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		Optional<SuiviSuperviseur> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		SuiviSuperviseur e = opt.get();
		if (canReadSuperviseur(user) || (canReadCentrale(user) && Boolean.TRUE.equals(e.getValideeSuperviseur()))) {
			return ResponseEntity.ok(toRow(e));
		}
		return forbidden("SUIVI_SUPERVISEUR:LIRE");
	}

	@Transactional
	@PostMapping
	public ResponseEntity<?> create(@RequestBody SuiviSuperviseurRequest body, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = requireSuperviseur(user, "CREER");
		if (denied != null) return denied;
		try {
			SuiviSuperviseur e = new SuiviSuperviseur();
			apply(e, body);
			e.setValideeSuperviseur(false);
			return ResponseEntity.status(201).body(toRow(repository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody SuiviSuperviseurRequest body, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = requireSuperviseur(user, "MODIFIER");
		if (denied != null) return denied;
		Optional<SuiviSuperviseur> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		SuiviSuperviseur e = opt.get();
		if (Boolean.TRUE.equals(e.getValideeSuperviseur())) {
			return ResponseEntity.badRequest().body(Map.of("message", "Modification impossible : le suivi superviseur est déjà validé."));
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
		ResponseEntity<?> denied = requireSuperviseur(user, "VALIDER");
		if (denied != null) return denied;
		Optional<SuiviSuperviseur> opt = repository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		SuiviSuperviseur e = opt.get();
		e.setValideeSuperviseur(true);
		return ResponseEntity.ok(toRow(repository.save(e)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = requireSuperviseur(user, "MODIFIER");
		if (denied != null) return denied;
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private ResponseEntity<?> requireSuperviseur(AuthUser user, String permission) {
		return user != null && user.hasPermission("SUIVI_SUPERVISEUR", permission)
				? null
				: forbidden("SUIVI_SUPERVISEUR:" + permission);
	}

	private boolean canReadSuperviseur(AuthUser user) {
		return user != null && user.hasPermission("SUIVI_SUPERVISEUR", "LIRE");
	}

	private boolean canReadCentrale(AuthUser user) {
		return user != null && user.hasPermission("SUIVI_CENTRALE", "LIRE");
	}

	private ResponseEntity<?> forbidden(String permission) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(Map.of("message", "Accès refusé: permission " + permission + " requise"));
	}

	private void apply(SuiviSuperviseur e, SuiviSuperviseurRequest r) {
		if (r == null || r.getIdAlpha() == null) throw new IllegalArgumentException("idAlpha est obligatoire");
		Alpha alpha = alphaRepository.findById(r.getIdAlpha()).orElseThrow(() -> new IllegalArgumentException("Alpha introuvable: " + r.getIdAlpha()));
		e.setIdAlpha(alpha);
		if (r.getIdPeriodeActivite() != null) {
			PeriodeActivite periode = periodeActiviteRepository.findById(r.getIdPeriodeActivite().longValue())
					.orElseThrow(() -> new IllegalArgumentException("Période d'activité introuvable: " + r.getIdPeriodeActivite()));
			e.setIdPeriodeActivite(periode);
		} else if (e.getId() == null) {
			throw new IllegalArgumentException("idPeriodeActivite est obligatoire");
		}
		e.setNombreVisiteConseillerSuperviseurEffectue(r.getNombreVisiteConseillerSuperviseurEffectue());
		e.setNombreReunionBilanConseillerSuperviseur(r.getNombreReunionBilanConseillerSuperviseur());
	}

	private Map<String, Object> toRow(SuiviSuperviseur e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "Alpha", e.getIdAlpha());
		ReferentielEnricher.putRef(m, "PeriodeActivite", e.getIdPeriodeActivite());
		m.put("nombreVisiteConseillerSuperviseurEffectue", e.getNombreVisiteConseillerSuperviseurEffectue());
		m.put("nombreReunionBilanConseillerSuperviseur", e.getNombreReunionBilanConseillerSuperviseur());
		m.put("valideeSuperviseur", Boolean.TRUE.equals(e.getValideeSuperviseur()));
		return m;
	}
}
