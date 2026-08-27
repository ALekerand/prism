package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.dcspa.prism.dto.LocaliteDImplantationRequest;
import com.dcspa.prism.entity.Commune;
import com.dcspa.prism.entity.LocaliteDImplantation;
import com.dcspa.prism.entity.MilieuImplantation;
import com.dcspa.prism.entity.SousPrefecture;
import com.dcspa.prism.repository.CommuneRepository;
import com.dcspa.prism.repository.MilieuImplantationRepository;
import com.dcspa.prism.repository.SousPrefectureRepository;
import com.dcspa.prism.service.LocaliteDImplantationService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/localite-d-implantation")
@RequiredArgsConstructor
public class LocaliteDImplantationController {

	private final LocaliteDImplantationService localiteDImplantationService;
	private final SousPrefectureRepository sousPrefectureRepository;
	private final MilieuImplantationRepository milieuImplantationRepository;
	private final CommuneRepository communeRepository;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> findAll() {
		List<Map<String, Object>> list = localiteDImplantationService.findAll().stream()
				.map(this::toLocaliteRow)
				.collect(Collectors.toList());
		return ResponseEntity.ok(list);
	}

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		return localiteDImplantationService.findById(id)
				.map(this::toLocaliteRow)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody LocaliteDImplantationRequest req) {
		LocaliteDImplantation entity = new LocaliteDImplantation();
		applyRequest(entity, req);
		return ResponseEntity.status(201).body(toLocaliteRow(localiteDImplantationService.save(entity)));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody LocaliteDImplantationRequest req) {
		LocaliteDImplantation existing = localiteDImplantationService.findById(id).orElse(null);
		if (existing == null) {
			return ResponseEntity.notFound().build();
		}
		applyRequest(existing, req);
		return ResponseEntity.ok(toLocaliteRow(localiteDImplantationService.save(existing)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		localiteDImplantationService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private Map<String, Object> toLocaliteRow(LocaliteDImplantation l) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", l.getId());
		m.put("codeLocalite", l.getCodeLocalite());
		m.put("nomLocalite", l.getNomLocalite());
		ReferentielEnricher.putRef(m, "SousPrefecture", l.getIdSousPrefecture());
		ReferentielEnricher.putRef(m, "MilieuImplantation", l.getIdMilieuImplentation());
		ReferentielEnricher.putRef(m, "Commune", l.getIdCommune());
		return m;
	}

	private void applyRequest(LocaliteDImplantation entity, LocaliteDImplantationRequest req) {
		if (req == null) {
			throw new IllegalArgumentException("Requête obligatoire");
		}
		if (req.getIdSousPrefecture() == null) {
			throw new IllegalArgumentException("idSousPrefecture est obligatoire");
		}
		if (req.getIdMilieuImplentation() == null) {
			throw new IllegalArgumentException("idMilieuImplentation est obligatoire");
		}
		SousPrefecture sp = sousPrefectureRepository.findById(req.getIdSousPrefecture())
				.orElseThrow(() -> new IllegalArgumentException("Sous-préfecture introuvable: " + req.getIdSousPrefecture()));
		MilieuImplantation mi = milieuImplantationRepository.findById(req.getIdMilieuImplentation())
				.orElseThrow(() -> new IllegalArgumentException("Milieu d’implantation introuvable: " + req.getIdMilieuImplentation()));

		Commune c = null;
		if (req.getIdCommune() != null) {
			c = communeRepository.findById(req.getIdCommune())
					.orElseThrow(() -> new IllegalArgumentException("Commune introuvable: " + req.getIdCommune()));
		}

		entity.setIdSousPrefecture(sp);
		entity.setIdMilieuImplentation(mi);
		entity.setIdCommune(c);
		entity.setNomLocalite(req.getNomLocalite());
	}
}
