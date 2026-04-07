package com.dcspa.prism.controller;
import com.dcspa.prism.controller.support.ReferentialPutHelper;

import com.dcspa.prism.dto.AlphaCreateRequest;
import com.dcspa.prism.dto.AlphaFullCreateRequest;
import com.dcspa.prism.dto.CentreCreatePayload;
import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.dto.UpdateLibelleRequest;
import com.dcspa.prism.entity.Campagne;
import com.dcspa.prism.entity.CategorieCentreAlpha;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.AutoriteAutorisation;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Iep;
import com.dcspa.prism.entity.LocaliteDImplantation;
import com.dcspa.prism.entity.Naturecentre;
import com.dcspa.prism.entity.Periodicite;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.entity.Regimealphabetisation;
import com.dcspa.prism.entity.TypeAlpha;
import com.dcspa.prism.repository.AutoriteAutorisationRepository;
import com.dcspa.prism.repository.CampagneRepository;
import com.dcspa.prism.repository.CategorieCentreAlphaRepository;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.IeppRepository;
import com.dcspa.prism.repository.LocaliteDImplantationRepository;
import com.dcspa.prism.repository.NaturecentreRepository;
import com.dcspa.prism.repository.PeriodiciteRepository;
import com.dcspa.prism.repository.PromoteurRepository;
import com.dcspa.prism.repository.RegimealphabetisationRepository;
import com.dcspa.prism.repository.TypeAlphaRepository;
import com.dcspa.prism.service.AlphaService;
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
@RequestMapping("/api/v1/alpha")
@RequiredArgsConstructor
public class AlphaController {

	private final AlphaService alphaService;
	private final CentreRepository centreRepository;
	private final CentreService centreService;
	private final CampagneRepository campagneRepository;
	private final CategorieCentreAlphaRepository categorieCentreAlphaRepository;
	private final TypeAlphaRepository typeAlphaRepository;
	private final RegimealphabetisationRepository regimealphabetisationRepository;
	private final PromoteurRepository promoteurRepository;
	private final LocaliteDImplantationRepository localiteRepository;
	private final IeppRepository iepRepository;
	private final NaturecentreRepository naturecentreRepository;
	private final PeriodiciteRepository periodiciteRepository;
	private final AutoriteAutorisationRepository autoriteAutorisationRepository;

	@GetMapping
	public ResponseEntity<List<CentreTypeListItem>> findAll() {
		List<CentreTypeListItem> list = alphaService.findAll().stream()
				.map(a -> new CentreTypeListItem(
						a.getId(),
						a.getCodeCentre(),
						a.getCodeAlpha(),
						a.getLibelleAlpha(),
						a.getIdLocalite(),
						a.getIdIep(),
						a.getIdNaturecentre(),
						a.getIdPeriodicite(),
						a.getIdAutoriteAutorisation(),
						a.getAutorisation(),
						a.getEstElectrifie(),
						a.getADeLeau(),
						a.getNombreVisite(),
						a.getLocalisationCentre(),
						a.getNomMilieuImplentation(),
						a.getEncadreurNonMena(),
						a.getEncadrerParMena()
				))
				.toList();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Alpha> findById(@PathVariable Integer id) {
		return alphaService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<CentreTypeListItem> create(@RequestBody AlphaCreateRequest req) {
		if (req == null || req.getCentreId() == null) {
			throw new IllegalArgumentException("centreId est obligatoire");
		}
		Centre centre = centreRepository.findById(req.getCentreId())
				.orElseThrow(() -> new IllegalArgumentException("Centre introuvable: " + req.getCentreId()));

		Campagne campagne = campagneRepository.findById(Objects.requireNonNull(req.getCampagneId(), "campagneId est obligatoire"))
				.orElseThrow(() -> new IllegalArgumentException("Campagne introuvable: " + req.getCampagneId()));
		CategorieCentreAlpha categorie = categorieCentreAlphaRepository.findById(Objects.requireNonNull(req.getCategorieCentreAlphaId(), "categorieCentreAlphaId est obligatoire"))
				.orElseThrow(() -> new IllegalArgumentException("Categorie centre alpha introuvable: " + req.getCategorieCentreAlphaId()));
		TypeAlpha typeAlpha = typeAlphaRepository.findById(Objects.requireNonNull(req.getTypeAlphaId(), "typeAlphaId est obligatoire").longValue())
				.orElseThrow(() -> new IllegalArgumentException("Type alpha introuvable: " + req.getTypeAlphaId()));
		Regimealphabetisation regime = regimealphabetisationRepository.findById(Objects.requireNonNull(req.getRegimeAlphaId(), "regimeAlphaId est obligatoire").longValue())
				.orElseThrow(() -> new IllegalArgumentException("Regime alpha introuvable: " + req.getRegimeAlphaId()));

		Alpha alpha = new Alpha();
		alpha.setCentre(centre);
		alpha.setIdCompagne(campagne);
		alpha.setIdCategorieCentreAlpha(categorie);
		alpha.setIdTypeAlpha(typeAlpha);
		alpha.setIdRegimeAlpha(regime);

		// Spécificités du centre : on copie les infos "centre" dans la table alpha
		alpha.setIdLocalite(centre.getIdLocalite() != null ? centre.getIdLocalite().getId() : null);
		alpha.setIdPeriodicite(centre.getIdPeriodicite() != null ? centre.getIdPeriodicite().getId() : null);
		alpha.setIdIep(centre.getIdIep() != null ? centre.getIdIep().getId() : null);
		alpha.setIdAutoriteAutorisation(centre.getIdAutoriteAutorisation() != null ? centre.getIdAutoriteAutorisation().getId() : null);
		alpha.setIdNaturecentre(centre.getIdNaturecentre() != null ? centre.getIdNaturecentre().getId() : null);
		alpha.setIdPromoteur(centre.getIdPromoteur() != null ? centre.getIdPromoteur().getId() : null);
		alpha.setCodeCentre(centre.getCodeCentre());
		alpha.setAutorisation(centre.getAutorisation());
		alpha.setEncadreurNonMena(centre.getEncadreurNonMena());
		alpha.setEncadrerParMena(centre.getEncadrerParMena());
		alpha.setEstElectrifie(centre.getEstElectrifie());
		alpha.setADeLeau(centre.getADeLeau());
		alpha.setNombreVisite(centre.getNombreVisite());
		alpha.setLocalisationCentre(centre.getLocalisationCentre());
		alpha.setNomMilieuImplentation(centre.getNomMilieuImplentation());
		alpha.setLibelleAlpha(req.getLibelleAlpha());

		Alpha saved = alphaService.save(alpha);
		return ResponseEntity.status(201).body(new CentreTypeListItem(
				saved.getId(),
				saved.getCodeCentre(),
				saved.getCodeAlpha(),
				saved.getLibelleAlpha(),
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
	 * Création "end-to-end" : promoteur + centre + alpha.
	 * Le codeAlpha est auto-généré (AutoCodeEntityListener).
	 */
	@PostMapping("/full")
	public ResponseEntity<CentreTypeListItem> createFull(@RequestBody AlphaFullCreateRequest req) {
		if (req == null) {
			throw new IllegalArgumentException("Requête obligatoire");
		}
		if (req.getCentre() == null) {
			throw new IllegalArgumentException("centre est obligatoire");
		}
		if (req.getPromoteur() == null) {
			throw new IllegalArgumentException("promoteur est obligatoire");
		}

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

		Campagne campagne = campagneRepository.findById(Objects.requireNonNull(req.getCampagneId(), "campagneId est obligatoire"))
				.orElseThrow(() -> new IllegalArgumentException("Campagne introuvable: " + req.getCampagneId()));
		CategorieCentreAlpha categorie = categorieCentreAlphaRepository.findById(Objects.requireNonNull(req.getCategorieCentreAlphaId(), "categorieCentreAlphaId est obligatoire"))
				.orElseThrow(() -> new IllegalArgumentException("Categorie centre alpha introuvable: " + req.getCategorieCentreAlphaId()));
		TypeAlpha typeAlpha = typeAlphaRepository.findById(Objects.requireNonNull(req.getTypeAlphaId(), "typeAlphaId est obligatoire").longValue())
				.orElseThrow(() -> new IllegalArgumentException("Type alpha introuvable: " + req.getTypeAlphaId()));
		Regimealphabetisation regime = regimealphabetisationRepository.findById(Objects.requireNonNull(req.getRegimeAlphaId(), "regimeAlphaId est obligatoire").longValue())
				.orElseThrow(() -> new IllegalArgumentException("Regime alpha introuvable: " + req.getRegimeAlphaId()));

		Alpha alpha = new Alpha();
		alpha.setCentre(savedCentre);
		alpha.setIdCompagne(campagne);
		alpha.setIdCategorieCentreAlpha(categorie);
		alpha.setIdTypeAlpha(typeAlpha);
		alpha.setIdRegimeAlpha(regime);

		// Spécificités du centre copiées dans alpha
		alpha.setIdLocalite(savedCentre.getIdLocalite() != null ? savedCentre.getIdLocalite().getId() : null);
		alpha.setIdPeriodicite(savedCentre.getIdPeriodicite() != null ? savedCentre.getIdPeriodicite().getId() : null);
		alpha.setIdIep(savedCentre.getIdIep() != null ? savedCentre.getIdIep().getId() : null);
		alpha.setIdAutoriteAutorisation(savedCentre.getIdAutoriteAutorisation() != null ? savedCentre.getIdAutoriteAutorisation().getId() : null);
		alpha.setIdNaturecentre(savedCentre.getIdNaturecentre() != null ? savedCentre.getIdNaturecentre().getId() : null);
		alpha.setIdPromoteur(savedCentre.getIdPromoteur() != null ? savedCentre.getIdPromoteur().getId() : null);
		alpha.setCodeCentre(savedCentre.getCodeCentre());
		alpha.setAutorisation(savedCentre.getAutorisation());
		alpha.setEncadreurNonMena(savedCentre.getEncadreurNonMena());
		alpha.setEncadrerParMena(savedCentre.getEncadrerParMena());
		alpha.setEstElectrifie(savedCentre.getEstElectrifie());
		alpha.setADeLeau(savedCentre.getADeLeau());
		alpha.setNombreVisite(savedCentre.getNombreVisite());
		alpha.setLocalisationCentre(savedCentre.getLocalisationCentre());
		alpha.setNomMilieuImplentation(savedCentre.getNomMilieuImplentation());
		alpha.setLibelleAlpha(req.getLibelleAlpha());

		Alpha savedAlpha = alphaService.save(alpha);
		return ResponseEntity.status(201).body(new CentreTypeListItem(
				savedAlpha.getId(),
				savedAlpha.getCodeCentre(),
				savedAlpha.getCodeAlpha(),
				savedAlpha.getLibelleAlpha(),
				savedAlpha.getIdLocalite(),
				savedAlpha.getIdIep(),
				savedAlpha.getIdNaturecentre(),
				savedAlpha.getIdPeriodicite(),
				savedAlpha.getIdAutoriteAutorisation(),
				savedAlpha.getAutorisation(),
				savedAlpha.getEstElectrifie(),
				savedAlpha.getADeLeau(),
				savedAlpha.getNombreVisite(),
				savedAlpha.getLocalisationCentre(),
				savedAlpha.getNomMilieuImplentation(),
				savedAlpha.getEncadreurNonMena(),
				savedAlpha.getEncadrerParMena()
		));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Alpha> update(@PathVariable Integer id, @RequestBody Alpha alpha) {
		return ReferentialPutHelper.putPreservingAutoCode(id, alpha, alphaService::findById, alphaService::save);
	}

	@PutMapping("/{id}/libelle")
	public ResponseEntity<CentreTypeListItem> updateLibelle(@PathVariable Integer id, @RequestBody UpdateLibelleRequest req) {
		Alpha existing = alphaService.findById(id).orElse(null);
		if (existing == null) return ResponseEntity.notFound().build();
		existing.setLibelleAlpha(req == null ? null : req.getLibelle());
		Alpha saved = alphaService.save(existing);
		return ResponseEntity.ok(new CentreTypeListItem(
				saved.getId(),
				saved.getCodeCentre(),
				saved.getCodeAlpha(),
				saved.getLibelleAlpha(),
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
		Alpha existing = alphaService.findById(id).orElse(null);
		if (existing == null) return ResponseEntity.notFound().build();
		if (req != null) {
			existing.setLibelleAlpha(req.getLibelle());
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
		Alpha saved = alphaService.save(existing);
		return ResponseEntity.ok(new CentreTypeListItem(
				saved.getId(),
				saved.getCodeCentre(),
				saved.getCodeAlpha(),
				saved.getLibelleAlpha(),
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
		alphaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
