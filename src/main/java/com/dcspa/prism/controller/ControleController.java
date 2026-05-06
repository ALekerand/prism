package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.JpaAssociationIds;
import com.dcspa.prism.dto.ControleRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.Controle;
import com.dcspa.prism.entity.Discipline;
import com.dcspa.prism.entity.Manuel;
import com.dcspa.prism.entity.NiveauControle;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.ControleRepository;
import com.dcspa.prism.repository.DisciplineRepository;
import com.dcspa.prism.repository.ManuelRepository;
import com.dcspa.prism.repository.NiveauControleRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/controle")
@RequiredArgsConstructor
public class ControleController {
	private final ControleRepository controleRepository;
	private final AlphaRepository alphaRepository;
	private final DisciplineRepository disciplineRepository;
	private final ManuelRepository manuelRepository;
	private final NiveauControleRepository niveauControleRepository;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		return ResponseEntity.ok(controleRepository.findAll().stream().map(this::toRow).toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return controleRepository.findById(id).map(this::toRow).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/search")
	public ResponseEntity<List<Map<String, Object>>> search(
			@RequestParam(required = false) Integer idAlpha,
			@RequestParam(required = false) Integer idDiscipline,
			@RequestParam(required = false) Integer idManuel,
			@RequestParam(required = false) Integer idNiveauControle,
			@RequestParam(required = false) Boolean conformiteProgramme) {
		return ResponseEntity.ok(controleRepository.findAll().stream()
				.filter(x -> idAlpha == null || idAlpha.equals(JpaAssociationIds.intIdOrNull(x.getIdAlpha())))
				.filter(x -> idDiscipline == null || idDiscipline.equals(JpaAssociationIds.intIdOrNull(x.getIdDiscipline())))
				.filter(x -> idManuel == null || idManuel.equals(JpaAssociationIds.intIdOrNull(x.getIdManuel())))
				.filter(x -> idNiveauControle == null || idNiveauControle.equals(JpaAssociationIds.intIdOrNull(x.getIdNiveauControle())))
				.filter(x -> conformiteProgramme == null || conformiteProgramme.equals(x.getConformiteProgramme()))
				.map(this::toRow)
				.toList());
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody ControleRequest body) {
		try {
			Controle e = new Controle();
			apply(e, body);
			return ResponseEntity.status(201).body(toRow(controleRepository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ControleRequest body) {
		Optional<Controle> opt = controleRepository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		try {
			Controle e = opt.get();
			apply(e, body);
			return ResponseEntity.ok(toRow(controleRepository.save(e)));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		controleRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private void apply(Controle e, ControleRequest r) {
		if (r == null || r.getIdAlpha() == null) throw new IllegalArgumentException("idAlpha est obligatoire");
		Alpha alpha = alphaRepository.findById(r.getIdAlpha()).orElseThrow(() -> new IllegalArgumentException("Alpha introuvable: " + r.getIdAlpha()));
		Discipline d = r.getIdDiscipline() == null ? null : disciplineRepository.findById(r.getIdDiscipline()).orElseThrow(() -> new IllegalArgumentException("Discipline introuvable: " + r.getIdDiscipline()));
		Manuel m = r.getIdManuel() == null ? null : manuelRepository.findById(r.getIdManuel()).orElseThrow(() -> new IllegalArgumentException("Manuel introuvable: " + r.getIdManuel()));
		NiveauControle n = r.getIdNiveauControle() == null ? null : niveauControleRepository.findById(r.getIdNiveauControle()).orElseThrow(() -> new IllegalArgumentException("Niveau controle introuvable: " + r.getIdNiveauControle()));
		e.setIdAlpha(alpha);
		e.setIdDiscipline(d);
		e.setIdManuel(m);
		e.setIdNiveauControle(n);
		e.setDateDemarrageAppren(r.getDateDemarrageAppren());
		e.setJourHeureFormation(r.getJourHeureFormation());
		e.setNombreKitManuelsSyllabaire(r.getNombreKitManuelsSyllabaire());
		e.setNombreKitManuelsCalculaire(r.getNombreKitManuelsCalculaire());
		e.setNombreKitManuelsCvc(r.getNombreKitManuelsCvc());
		e.setNombreKitAutre(r.getNombreKitAutre());
		e.setConformiteProgramme(r.getConformiteProgramme());
	}

	private Map<String, Object> toRow(Controle e) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", e.getId());
		m.put("idAlpha", JpaAssociationIds.intIdOrNull(e.getIdAlpha()));
		m.put("idDiscipline", JpaAssociationIds.intIdOrNull(e.getIdDiscipline()));
		m.put("idManuel", JpaAssociationIds.intIdOrNull(e.getIdManuel()));
		m.put("idNiveauControle", JpaAssociationIds.intIdOrNull(e.getIdNiveauControle()));
		m.put("dateDemarrageAppren", e.getDateDemarrageAppren());
		m.put("jourHeureFormation", e.getJourHeureFormation());
		m.put("nombreKitManuelsSyllabaire", e.getNombreKitManuelsSyllabaire());
		m.put("nombreKitManuelsCalculaire", e.getNombreKitManuelsCalculaire());
		m.put("nombreKitManuelsCvc", e.getNombreKitManuelsCvc());
		m.put("nombreKitAutre", e.getNombreKitAutre());
		m.put("conformiteProgramme", e.getConformiteProgramme());
		return m;
	}
}
