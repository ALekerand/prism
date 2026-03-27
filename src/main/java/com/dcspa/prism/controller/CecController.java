package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.CentreCreatePayload;
import com.dcspa.prism.dto.SimpleCentreCreateRequest;
import com.dcspa.prism.dto.SimpleCentreTypeFullCreateRequest;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.dto.UpdateLibelleRequest;
import com.dcspa.prism.entity.AutoriteAutorisation;
import com.dcspa.prism.entity.Cec;
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
import com.dcspa.prism.service.CecService;
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
@RequestMapping("/api/cec")
@RequiredArgsConstructor
public class CecController {

	private final CecService cecService;
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
		List<CentreTypeListItem> list = cecService.findAll().stream()
				.map(c -> new CentreTypeListItem(
						c.getId(),
						c.getCodeCentre(),
						null,
						c.getLibelleCec(),
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
						c.getNomMilieuImplentation()
				))
				.toList();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cec> findById(@PathVariable Integer id) {
		return cecService.findById(id)
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

		Cec c = new Cec();
		c.setCentre(centre);
		c.setIdLocalite(centre.getIdLocalite() != null ? centre.getIdLocalite().getId() : null);
		c.setIdPeriodicite(centre.getIdPeriodicite() != null ? centre.getIdPeriodicite().getId() : null);
		c.setIdIep(centre.getIdIep() != null ? centre.getIdIep().getId() : null);
		c.setIdAutoriteAutorisation(centre.getIdAutoriteAutorisation() != null ? centre.getIdAutoriteAutorisation().getId() : null);
		c.setIdNaturecentre(centre.getIdNaturecentre() != null ? centre.getIdNaturecentre().getId() : null);
		c.setIdPromoteur(centre.getIdPromoteur() != null ? centre.getIdPromoteur().getId() : null);
		c.setCodeCentre(centre.getCodeCentre());
		c.setAutorisation(centre.getAutorisation());
		c.setEncadreurNonMena(centre.getEncadreurNonMena());
		c.setEncadrerParMena(centre.getEncadrerParMena());
		c.setEstElectrifie(centre.getEstElectrifie());
		c.setADeLeau(centre.getADeLeau());
		c.setNombreVisite(centre.getNombreVisite());
		c.setLocalisationCentre(centre.getLocalisationCentre());
		c.setNomMilieuImplentation(centre.getNomMilieuImplentation());
		c.setLibelleCec(req.getLibelle());
		Cec saved = cecService.save(c);

		return ResponseEntity.status(201).body(new CentreTypeListItem(
				saved.getId(),
				saved.getCodeCentre(),
				null,
				saved.getLibelleCec(),
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
				saved.getNomMilieuImplentation()
		));
	}

	/**
	 * Création "end-to-end" : promoteur + centre + cec.
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

		Cec entity = new Cec();
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
		entity.setLibelleCec(req.getLibelle());

		Cec saved = cecService.save(entity);
		return ResponseEntity.status(201).body(new CentreTypeListItem(
				saved.getId(),
				saved.getCodeCentre(),
				null,
				saved.getLibelleCec(),
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
				saved.getNomMilieuImplentation()
		));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cec> update(@PathVariable Integer id, @RequestBody Cec body) {
		return ReferentialPutHelper.putPreservingAutoCode(id, body, cecService::findById, cecService::save);
	}

	@PutMapping("/{id}/libelle")
	public ResponseEntity<CentreTypeListItem> updateLibelle(@PathVariable Integer id, @RequestBody UpdateLibelleRequest req) {
		Cec existing = cecService.findById(id).orElse(null);
		if (existing == null) return ResponseEntity.notFound().build();
		existing.setLibelleCec(req == null ? null : req.getLibelle());
		Cec saved = cecService.save(existing);
		return ResponseEntity.ok(new CentreTypeListItem(
				saved.getId(),
				saved.getCodeCentre(),
				null,
				saved.getLibelleCec(),
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
				saved.getNomMilieuImplentation()
		));
	}

	@PutMapping("/{id}/infos")
	public ResponseEntity<CentreTypeListItem> updateInfos(@PathVariable Integer id, @RequestBody UpdateCentreTypeInfosRequest req) {
		Cec existing = cecService.findById(id).orElse(null);
		if (existing == null) return ResponseEntity.notFound().build();
		if (req != null) {
			existing.setLibelleCec(req.getLibelle());
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
		}
		Cec saved = cecService.save(existing);
		return ResponseEntity.ok(new CentreTypeListItem(
				saved.getId(),
				saved.getCodeCentre(),
				null,
				saved.getLibelleCec(),
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
				saved.getNomMilieuImplentation()
		));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		cecService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
