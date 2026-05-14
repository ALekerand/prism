package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ActivitesCentreSaisieWorkflowGate;
import com.dcspa.prism.controller.support.ActivitesCentreWorkflow;
import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.controller.support.PermissionGuard;
import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.ControleRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.Controle;
import com.dcspa.prism.entity.ControleHoraireFormation;
import com.dcspa.prism.entity.ControleKitManuel;
import com.dcspa.prism.entity.Discipline;
import com.dcspa.prism.entity.Manuel;
import com.dcspa.prism.entity.NiveauAlpha;
import com.dcspa.prism.entity.NiveauControle;
import com.dcspa.prism.entity.PeriodeActivite;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.ControleRepository;
import com.dcspa.prism.repository.DisciplineRepository;
import com.dcspa.prism.repository.ManuelRepository;
import com.dcspa.prism.repository.NiveauAlphaRepository;
import com.dcspa.prism.repository.NiveauControleRepository;
import com.dcspa.prism.repository.PeriodeActiviteRepository;
import com.dcspa.prism.security.AuthUser;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/controle")
@RequiredArgsConstructor
public class ControleController {
	private static final String FEATURE = "ACTIVITES_CENTRE_CONTROLE";
	private static final String WORKFLOW_RESOURCE = "/api/controle";
	private final ControleRepository controleRepository;
	private final ActivitesCentreSaisieWorkflowGate saisieWorkflowGate;
	private final AlphaRepository alphaRepository;
	private final DisciplineRepository disciplineRepository;
	private final ManuelRepository manuelRepository;
	private final NiveauAlphaRepository niveauAlphaRepository;
	private final NiveauControleRepository niveauControleRepository;
	private final PeriodeActiviteRepository periodeActiviteRepository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<?> findAll(@AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "LIRE");
		if (denied != null) return denied;
		return ResponseEntity.ok(controleRepository.findAllWithRefs().stream().map(this::toRow).toList());
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "LIRE");
		if (denied != null) return denied;
		return controleRepository.findByIdWithRefs(id).map(this::toRow).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@Transactional(readOnly = true)
	@GetMapping("/search")
	public ResponseEntity<?> search(
			@RequestParam(required = false) Integer idAlpha,
			@RequestParam(required = false) Integer idDiscipline,
			@RequestParam(required = false) Integer idManuel,
			@RequestParam(required = false) Integer idNiveauControle,
			@RequestParam(required = false) Integer idPeriodeActivite,
			@RequestParam(required = false) Boolean conformiteProgramme,
			@AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "LIRE");
		if (denied != null) return denied;
		return ResponseEntity.ok(controleRepository.findAllWithRefs().stream()
				.filter(x -> idAlpha == null || idAlpha.equals(JpaAssociationIds.intIdOrNull(x.getIdAlpha())))
				.filter(x -> idDiscipline == null || idDiscipline.equals(JpaAssociationIds.intIdOrNull(x.getIdDiscipline())))
				.filter(x -> idManuel == null || idManuel.equals(JpaAssociationIds.intIdOrNull(x.getIdManuel())))
				.filter(x -> idNiveauControle == null || idNiveauControle.equals(JpaAssociationIds.intIdOrNull(x.getIdNiveauControle())))
				.filter(x -> idPeriodeActivite == null
						|| idPeriodeActivite.equals(JpaAssociationIds.intIdOrNull(x.getIdPeriodeActivite())))
				.filter(x -> conformiteProgramme == null || conformiteProgramme.equals(x.getConformiteProgramme()))
				.map(this::toRow)
				.toList());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<?> create(@RequestBody ControleRequest body, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "CREER");
		if (denied != null) return denied;
		try {
			Controle e = new Controle();
			apply(e, body);
			ActivitesCentreWorkflow.initializeDraft(e);
			return ResponseEntity.status(201).body(toRow(controleRepository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ControleRequest body, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "MODIFIER");
		if (denied != null) return denied;
		Optional<Controle> opt = controleRepository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		try {
			Controle e = opt.get();
			ActivitesCentreWorkflow.ensureEditable(e);
			apply(e, body);
			return ResponseEntity.ok(toRow(controleRepository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "MODIFIER");
		if (denied != null) return denied;
		Optional<Controle> opt = controleRepository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		try {
			ActivitesCentreWorkflow.ensureEditable(opt.get());
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
		controleRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Transactional
	@PutMapping("/{id}/valider-coordonnateur")
	public ResponseEntity<?> validateCoordonnateur(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		Optional<Controle> opt = controleRepository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		Optional<ResponseEntity<?>> workflowDenied =
				saisieWorkflowGate.transversalValidate(WORKFLOW_RESOURCE, id, FEATURE, user);
		if (workflowDenied.isPresent()) return workflowDenied.get();
		Controle e = opt.get();
		ResponseEntity<?> denied = ActivitesCentreWorkflow.validateCoordonnateur(e, user, FEATURE);
		if (denied != null) return denied;
		return ResponseEntity.ok(toRow(controleRepository.save(e)));
	}

	@Transactional
	@PutMapping("/{id}/valider-superviseur")
	public ResponseEntity<?> validateSuperviseur(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		Optional<Controle> opt = controleRepository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		Optional<ResponseEntity<?>> workflowDenied =
				saisieWorkflowGate.transversalValidate(WORKFLOW_RESOURCE, id, FEATURE, user);
		if (workflowDenied.isPresent()) return workflowDenied.get();
		Controle e = opt.get();
		ResponseEntity<?> denied = ActivitesCentreWorkflow.validateSuperviseur(e, user, FEATURE);
		if (denied != null) return denied;
		return ResponseEntity.ok(toRow(controleRepository.save(e)));
	}

	@Transactional
	@PutMapping("/{id}/valider-centrale")
	public ResponseEntity<?> validateCentrale(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user) {
		Optional<Controle> opt = controleRepository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		Optional<ResponseEntity<?>> workflowDenied =
				saisieWorkflowGate.transversalValidate(WORKFLOW_RESOURCE, id, FEATURE, user);
		if (workflowDenied.isPresent()) return workflowDenied.get();
		Controle e = opt.get();
		ResponseEntity<?> denied = ActivitesCentreWorkflow.validateCentrale(e, user, FEATURE);
		if (denied != null) return denied;
		return ResponseEntity.ok(toRow(controleRepository.save(e)));
	}

	private void apply(Controle e, ControleRequest r) {
		if (r == null || r.getIdAlpha() == null) throw new IllegalArgumentException("idAlpha est obligatoire");
		Alpha alpha = alphaRepository.findById(r.getIdAlpha()).orElseThrow(() -> new IllegalArgumentException("Alpha introuvable: " + r.getIdAlpha()));
		Discipline d = r.getIdDiscipline() == null ? null : disciplineRepository.findById(r.getIdDiscipline()).orElseThrow(() -> new IllegalArgumentException("Discipline introuvable: " + r.getIdDiscipline()));
		Manuel m = r.getIdManuel() == null ? null : manuelRepository.findById(r.getIdManuel()).orElseThrow(() -> new IllegalArgumentException("Manuel introuvable: " + r.getIdManuel()));
		NiveauControle n = r.getIdNiveauControle() == null ? null : niveauControleRepository.findById(r.getIdNiveauControle()).orElseThrow(() -> new IllegalArgumentException("Niveau controle introuvable: " + r.getIdNiveauControle()));
		NiveauAlpha niveauAlpha = r.getIdNiveauAlpha() == null ? null : niveauAlphaRepository.findById(r.getIdNiveauAlpha()).orElseThrow(() -> new IllegalArgumentException("Niveau Alpha introuvable: " + r.getIdNiveauAlpha()));
		e.setIdAlpha(alpha);
		if (r.getIdPeriodeActivite() != null) {
			PeriodeActivite periode = periodeActiviteRepository.findById(r.getIdPeriodeActivite().longValue())
					.orElseThrow(() -> new IllegalArgumentException("Période d'activité introuvable: " + r.getIdPeriodeActivite()));
			e.setIdPeriodeActivite(periode);
		} else if (e.getId() == null) {
			throw new IllegalArgumentException("idPeriodeActivite est obligatoire");
		}
		e.setIdDiscipline(d);
		e.setIdManuel(m);
		e.setIdNiveauControle(n);
		e.setIdNiveauAlpha(niveauAlpha);
		e.setDateDemarrageAppren(r.getDateDemarrageAppren());
		e.setJourHeureFormation(r.getJourHeureFormation());
		e.setNombreKitManuelsSyllabaire(r.getNombreKitManuelsSyllabaire());
		e.setNombreKitManuelsCalculaire(r.getNombreKitManuelsCalculaire());
		e.setNombreKitManuelsCvc(r.getNombreKitManuelsCvc());
		e.setNombreKitAutre(r.getNombreKitAutre());
		e.setConformiteProgramme(r.getConformiteProgramme());
		if (r.getHorairesFormation() != null) {
			replaceHoraires(e, r);
		}
		if (r.getKitsManuels() != null) {
			replaceKits(e, r);
		}
	}

	private void replaceHoraires(Controle e, ControleRequest r) {
		e.getHorairesFormation().clear();
		for (ControleRequest.HoraireFormationRequest item : r.getHorairesFormation()) {
			if (item == null) continue;
			String jour = item.getJourSemaine() == null ? "" : item.getJourSemaine().trim().toUpperCase();
			LocalTime debut = item.getHeureDebut();
			LocalTime fin = item.getHeureFin();
			if (jour.isBlank()) throw new IllegalArgumentException("Le jour de formation est obligatoire");
			if (debut == null || fin == null) throw new IllegalArgumentException("Les heures début/fin sont obligatoires pour " + jour);
			if (!fin.isAfter(debut)) throw new IllegalArgumentException("L'heure de fin doit être supérieure à l'heure de début pour " + jour);
			ControleHoraireFormation h = new ControleHoraireFormation();
			h.setControle(e);
			h.setJourSemaine(jour);
			h.setHeureDebut(debut);
			h.setHeureFin(fin);
			e.getHorairesFormation().add(h);
		}
	}

	private void replaceKits(Controle e, ControleRequest r) {
		e.getKitsManuels().clear();
		for (ControleRequest.KitManuelRequest item : r.getKitsManuels()) {
			if (item == null) continue;
			if (item.getIdManuel() == null) throw new IllegalArgumentException("Le manuel est obligatoire pour chaque kit");
			Integer nombre = item.getNombreKit();
			if (nombre == null || nombre < 0) throw new IllegalArgumentException("Le nombre de kits doit être positif ou nul");
			Manuel manuel = manuelRepository.findById(item.getIdManuel())
					.orElseThrow(() -> new IllegalArgumentException("Manuel introuvable: " + item.getIdManuel()));
			ControleKitManuel k = new ControleKitManuel();
			k.setControle(e);
			k.setManuel(manuel);
			k.setNombreKit(nombre);
			k.setPrecisionAutre(item.getPrecisionAutre());
			e.getKitsManuels().add(k);
		}
	}

	private Map<String, Object> toRow(Controle e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		ReferentielEnricher.putRef(m, "Alpha", e.getIdAlpha());
		ReferentielEnricher.putRef(m, "PeriodeActivite", e.getIdPeriodeActivite());
		ReferentielEnricher.putRef(m, "Discipline", e.getIdDiscipline());
		ReferentielEnricher.putRef(m, "Manuel", e.getIdManuel());
		ReferentielEnricher.putRef(m, "NiveauControle", e.getIdNiveauControle());
		ReferentielEnricher.putRef(m, "NiveauAlpha", e.getIdNiveauAlpha());
		m.put("dateDemarrageAppren", e.getDateDemarrageAppren());
		m.put("jourHeureFormation", e.getJourHeureFormation());
		m.put("nombreKitManuelsSyllabaire", e.getNombreKitManuelsSyllabaire());
		m.put("nombreKitManuelsCalculaire", e.getNombreKitManuelsCalculaire());
		m.put("nombreKitManuelsCvc", e.getNombreKitManuelsCvc());
		m.put("nombreKitAutre", e.getNombreKitAutre());
		m.put("conformiteProgramme", e.getConformiteProgramme());
		ActivitesCentreWorkflow.putStatus(m, e);
		m.put("horairesFormation", e.getHorairesFormation().stream().map(this::toHoraireRow).toList());
		m.put("kitsManuels", e.getKitsManuels().stream().map(this::toKitRow).toList());
		return m;
	}

	private Map<String, Object> toHoraireRow(ControleHoraireFormation h) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", h.getId());
		m.put("jourSemaine", h.getJourSemaine());
		m.put("heureDebut", h.getHeureDebut());
		m.put("heureFin", h.getHeureFin());
		return m;
	}

	private Map<String, Object> toKitRow(ControleKitManuel k) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", k.getId());
		ReferentielEnricher.putRef(m, "Manuel", k.getManuel());
		m.put("nombreKit", k.getNombreKit());
		m.put("precisionAutre", k.getPrecisionAutre());
		return m;
	}
}
