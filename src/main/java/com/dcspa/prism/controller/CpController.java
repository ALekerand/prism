package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.CentreCreatePayload;
import com.dcspa.prism.dto.SimpleCentreCreateRequest;
import com.dcspa.prism.dto.SimpleCentreTypeFullCreateRequest;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.dto.UpdateLibelleRequest;
import com.dcspa.prism.entity.AutoriteAutorisation;
import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Iep;
import com.dcspa.prism.entity.LocaliteDImplantation;
import com.dcspa.prism.entity.Naturecentre;
import com.dcspa.prism.entity.Periodicite;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.repository.AutoriteAutorisationRepository;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.IeppRepository;
import com.dcspa.prism.repository.LocaliteDImplantationRepository;
import com.dcspa.prism.repository.NaturecentreRepository;
import com.dcspa.prism.repository.PeriodiciteRepository;
import com.dcspa.prism.repository.PromoteurRepository;
import com.dcspa.prism.service.CpService;
import com.dcspa.prism.service.CentreService;
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
import java.util.Objects;

@RestController
@RequestMapping("/api/cp")
@RequiredArgsConstructor
public class CpController {

	private final CpService cpService;
	private final CentreRepository centreRepository;
	private final CentreService centreService;
	private final PromoteurRepository promoteurRepository;
	private final LocaliteDImplantationRepository localiteRepository;
	private final IeppRepository iepRepository;
	private final NaturecentreRepository naturecentreRepository;
	private final PeriodiciteRepository periodiciteRepository;
	private final AutoriteAutorisationRepository autoriteAutorisationRepository;

	@GetMapping
	public ResponseEntity<List<CentreTypeListItem>> findAll() {
		List<CentreTypeListItem> list = cpService.findAll().stream()
				.map(c -> new CentreTypeListItem(
						c.getId(),
						c.getCodeCentre(),
						null,
						c.getLibellleCp(),
						c.getIdLocalite(),
						c.getIdIep(),
						c.getIdNaturecentre(),
						c.getIdPeriodicite(),
						c.getIdAutoriteAutorisation(),
						c.getAutorisation(),
						c.getEstElectrifie(),
						c.getADeLeau(),
						c.getNombreVisite(),
						c.getLocalisationCentre(),
						c.getNomMilieuImplentation(),
						c.getEncadreurNonMena(),
						c.getEncadrerParMena()
				))
				.toList();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cp> findById(@PathVariable Integer id) {
		return cpService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<CentreTypeListItem> create(@RequestBody SimpleCentreCreateRequest req) {
		if (req == null || req.getCentreId() == null) {
			throw new IllegalArgumentException("centreId est obligatoire");
		}
		Centre centre = centreRepository.findById(req.getCentreId())
				.orElseThrow(() -> new IllegalArgumentException("Centre introuvable: " + req.getCentreId()));

		Cp cp = new Cp();
		cp.setCentre(centre);
		cp.setIdLocalite(centre.getIdLocalite() != null ? centre.getIdLocalite().getId() : null);
		cp.setIdPeriodicite(centre.getIdPeriodicite() != null ? centre.getIdPeriodicite().getId() : null);
		cp.setIdIep(centre.getIdIep() != null ? centre.getIdIep().getId() : null);
		cp.setIdAutoriteAutorisation(centre.getIdAutoriteAutorisation() != null ? centre.getIdAutoriteAutorisation().getId() : null);
		cp.setIdNaturecentre(centre.getIdNaturecentre() != null ? centre.getIdNaturecentre().getId() : null);
		cp.setIdPromoteur(centre.getIdPromoteur() != null ? centre.getIdPromoteur().getId() : null);
		cp.setCodeCentre(centre.getCodeCentre());
		cp.setAutorisation(centre.getAutorisation());
		cp.setEncadreurNonMena(centre.getEncadreurNonMena());
		cp.setEncadrerParMena(centre.getEncadrerParMena());
		cp.setEstElectrifie(centre.getEstElectrifie());
		cp.setADeLeau(centre.getADeLeau());
		cp.setNombreVisite(centre.getNombreVisite());
		cp.setLocalisationCentre(centre.getLocalisationCentre());
		cp.setNomMilieuImplentation(centre.getNomMilieuImplentation());
		cp.setLibellleCp(req.getLibelle());
		Cp saved = cpService.save(cp);

		return ResponseEntity.status(201).body(new CentreTypeListItem(
				saved.getId(),
				saved.getCodeCentre(),
				null,
				saved.getLibellleCp(),
				saved.getIdLocalite(),
				saved.getIdIep(),
				saved.getIdNaturecentre(),
				saved.getIdPeriodicite(),
				saved.getIdAutoriteAutorisation(),
				saved.getAutorisation(),
				saved.getEstElectrifie(),
				saved.getADeLeau(),
				saved.getNombreVisite(),
				saved.getLocalisationCentre(),
				saved.getNomMilieuImplentation(),
				saved.getEncadreurNonMena(),
				saved.getEncadrerParMena()
		));
	}

	/**
	 * Création "end-to-end" : promoteur + centre + cp.
	 */
	@PostMapping("/full")
	public ResponseEntity<CentreTypeListItem> createFull(@RequestBody SimpleCentreTypeFullCreateRequest req) {
		if (req == null) throw new IllegalArgumentException("Requête obligatoire");
		if (req.getCentre() == null) throw new IllegalArgumentException("centre est obligatoire");
		if (req.getPromoteur() == null) throw new IllegalArgumentException("promoteur est obligatoire");

		CentreCreatePayload c = req.getCentre();

		Promoteur promoteur;
		if (req.getPromoteur().getId() != null) {
			promoteur = promoteurRepository.findById(req.getPromoteur().getId())
					.orElseThrow(() -> new IllegalArgumentException("Promoteur introuvable: " + req.getPromoteur().getId()));
		} else {
			promoteur = new Promoteur();
			promoteur.setCodePromoteur(req.getPromoteur().getCodePromoteur());
			promoteur.setLibellePromoteur(req.getPromoteur().getLibellePromoteur());
			promoteur = promoteurRepository.save(promoteur);
		}

		LocaliteDImplantation localite = localiteRepository.findById(Objects.requireNonNull(c.getLocaliteId(), "localiteId est obligatoire"))
				.orElseThrow(() -> new IllegalArgumentException("Localite introuvable: " + c.getLocaliteId()));
		Iep iep = iepRepository.findById(Objects.requireNonNull(c.getIepId(), "iepId est obligatoire"))
				.orElseThrow(() -> new IllegalArgumentException("IEP introuvable: " + c.getIepId()));
		Naturecentre nature = naturecentreRepository.findById(Objects.requireNonNull(c.getNatureCentreId(), "natureCentreId est obligatoire"))
				.orElseThrow(() -> new IllegalArgumentException("Nature centre introuvable: " + c.getNatureCentreId()));

		Periodicite periodicite = null;
		if (c.getPeriodiciteId() != null) {
			periodicite = periodiciteRepository.findById(c.getPeriodiciteId().longValue())
					.orElseThrow(() -> new IllegalArgumentException("Periodicite introuvable: " + c.getPeriodiciteId()));
		}

		AutoriteAutorisation autorite = null;
		if (c.getAutoriteAutorisationId() != null) {
			autorite = autoriteAutorisationRepository.findById(c.getAutoriteAutorisationId())
					.orElseThrow(() -> new IllegalArgumentException("Autorite autorisation introuvable: " + c.getAutoriteAutorisationId()));
		}

		Centre centre = new Centre();
		centre.setIdLocalite(localite);
		centre.setIdIep(iep);
		centre.setIdNaturecentre(nature);
		centre.setIdPromoteur(promoteur);
		centre.setIdPeriodicite(periodicite);
		centre.setIdAutoriteAutorisation(autorite);

		// codeCentre auto-généré si non fourni
		centre.setCodeCentre(c.getCodeCentre());
		centre.setAutorisation(c.getAutorisation());
		centre.setEncadreurNonMena(c.getEncadreurNonMena());
		centre.setEncadrerParMena(c.getEncadrerParMena());
		centre.setEstElectrifie(c.getEstElectrifie());
		centre.setADeLeau(c.getADeLeau());
		centre.setNombreVisite(c.getNombreVisite());
		centre.setLocalisationCentre(c.getLocalisationCentre());
		centre.setNomMilieuImplentation(c.getNomMilieuImplentation());

		Centre savedCentre = centreService.save(centre);

		Cp entity = new Cp();
		entity.setCentre(savedCentre);
		entity.setIdLocalite(savedCentre.getIdLocalite() != null ? savedCentre.getIdLocalite().getId() : null);
		entity.setIdPeriodicite(savedCentre.getIdPeriodicite() != null ? savedCentre.getIdPeriodicite().getId() : null);
		entity.setIdIep(savedCentre.getIdIep() != null ? savedCentre.getIdIep().getId() : null);
		entity.setIdAutoriteAutorisation(savedCentre.getIdAutoriteAutorisation() != null ? savedCentre.getIdAutoriteAutorisation().getId() : null);
		entity.setIdNaturecentre(savedCentre.getIdNaturecentre() != null ? savedCentre.getIdNaturecentre().getId() : null);
		entity.setIdPromoteur(savedCentre.getIdPromoteur() != null ? savedCentre.getIdPromoteur().getId() : null);
		entity.setCodeCentre(savedCentre.getCodeCentre());
		entity.setAutorisation(savedCentre.getAutorisation());
		entity.setEncadreurNonMena(savedCentre.getEncadreurNonMena());
		entity.setEncadrerParMena(savedCentre.getEncadrerParMena());
		entity.setEstElectrifie(savedCentre.getEstElectrifie());
		entity.setADeLeau(savedCentre.getADeLeau());
		entity.setNombreVisite(savedCentre.getNombreVisite());
		entity.setLocalisationCentre(savedCentre.getLocalisationCentre());
		entity.setNomMilieuImplentation(savedCentre.getNomMilieuImplentation());
		entity.setLibellleCp(req.getLibelle());

		Cp saved = cpService.save(entity);
		return ResponseEntity.status(201).body(new CentreTypeListItem(
				saved.getId(),
				saved.getCodeCentre(),
				null,
				saved.getLibellleCp(),
				saved.getIdLocalite(),
				saved.getIdIep(),
				saved.getIdNaturecentre(),
				saved.getIdPeriodicite(),
				saved.getIdAutoriteAutorisation(),
				saved.getAutorisation(),
				saved.getEstElectrifie(),
				saved.getADeLeau(),
				saved.getNombreVisite(),
				saved.getLocalisationCentre(),
				saved.getNomMilieuImplentation(),
				saved.getEncadreurNonMena(),
				saved.getEncadrerParMena()
		));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cp> update(@PathVariable Integer id, @RequestBody Cp body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, cpService::findById, cpService::save);
	}

	@PutMapping("/{id}/libelle")
	public ResponseEntity<CentreTypeListItem> updateLibelle(@PathVariable Integer id, @RequestBody UpdateLibelleRequest req) {
		Cp existing = cpService.findById(id).orElse(null);
		if (existing == null) return ResponseEntity.notFound().build();
		existing.setLibellleCp(req == null ? null : req.getLibelle());
		Cp saved = cpService.save(existing);
		return ResponseEntity.ok(new CentreTypeListItem(
				saved.getId(),
				saved.getCodeCentre(),
				null,
				saved.getLibellleCp(),
				saved.getIdLocalite(),
				saved.getIdIep(),
				saved.getIdNaturecentre(),
				saved.getIdPeriodicite(),
				saved.getIdAutoriteAutorisation(),
				saved.getAutorisation(),
				saved.getEstElectrifie(),
				saved.getADeLeau(),
				saved.getNombreVisite(),
				saved.getLocalisationCentre(),
				saved.getNomMilieuImplentation(),
				saved.getEncadreurNonMena(),
				saved.getEncadrerParMena()
		));
	}

	@PutMapping("/{id}/infos")
	public ResponseEntity<CentreTypeListItem> updateInfos(@PathVariable Integer id, @RequestBody UpdateCentreTypeInfosRequest req) {
		Cp existing = cpService.findById(id).orElse(null);
		if (existing == null) return ResponseEntity.notFound().build();
		if (req != null) {
			existing.setLibellleCp(req.getLibelle());
			existing.setIdLocalite(req.getIdLocalite());
			existing.setIdIep(req.getIdIep());
			existing.setIdNaturecentre(req.getIdNaturecentre());
			existing.setIdPeriodicite(req.getIdPeriodicite());
			existing.setIdAutoriteAutorisation(req.getIdAutoriteAutorisation());
			existing.setAutorisation(req.getAutorisation());
			existing.setEstElectrifie(req.getEstElectrifie());
			existing.setADeLeau(req.getADeLeau());
			existing.setNombreVisite(req.getNombreVisite());
			existing.setLocalisationCentre(req.getLocalisationCentre());
			existing.setNomMilieuImplentation(req.getNomMilieuImplentation());
			existing.setEncadreurNonMena(req.getEncadreurNonMena());
			existing.setEncadrerParMena(req.getEncadrerParMena());
		}
		Cp saved = cpService.save(existing);
		return ResponseEntity.ok(new CentreTypeListItem(
				saved.getId(),
				saved.getCodeCentre(),
				null,
				saved.getLibellleCp(),
				saved.getIdLocalite(),
				saved.getIdIep(),
				saved.getIdNaturecentre(),
				saved.getIdPeriodicite(),
				saved.getIdAutoriteAutorisation(),
				saved.getAutorisation(),
				saved.getEstElectrifie(),
				saved.getADeLeau(),
				saved.getNombreVisite(),
				saved.getLocalisationCentre(),
				saved.getNomMilieuImplentation(),
				saved.getEncadreurNonMena(),
				saved.getEncadrerParMena()
		));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		cpService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
