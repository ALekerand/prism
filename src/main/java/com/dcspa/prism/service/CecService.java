package com.dcspa.prism.service;

import com.dcspa.prism.dto.CecListFilter;
import com.dcspa.prism.dto.CentreCreatePayload;
import com.dcspa.prism.dto.CentreTypeListItem;
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
import com.dcspa.prism.repository.CecRepository;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.IeppRepository;
import com.dcspa.prism.repository.LocaliteDImplantationRepository;
import com.dcspa.prism.repository.NaturecentreRepository;
import com.dcspa.prism.repository.PeriodiciteRepository;
import com.dcspa.prism.repository.PromoteurRepository;
import com.dcspa.prism.repository.spec.SimpleCentreTypeSpecifications;
import com.dcspa.prism.service.pagination.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CecService {

	private final CecRepository cecRepository;
	private final CentreRepository centreRepository;
	private final CentreService centreService;
	private final PromoteurRepository promoteurRepository;
	private final LocaliteDImplantationRepository localiteRepository;
	private final IeppRepository iepRepository;
	private final NaturecentreRepository naturecentreRepository;
	private final PeriodiciteRepository periodiciteRepository;
	private final AutoriteAutorisationRepository autoriteAutorisationRepository;

	// Charge tous les CEC.
	@Transactional(readOnly = true)
	public List<Cec> findAll() {
		return cecRepository.findAll();
	}

	// Liste paginée avec filtres optionnels sur chaque colonne.
	@Transactional(readOnly = true)
	public Page<CentreTypeListItem> findAllListItems(Pageable pageable, CecListFilter filter) {
		Pageable p = PageableUtils.cap(pageable);
		Specification<Cec> spec = SimpleCentreTypeSpecifications.forCec(filter);
		return cecRepository.findAll(spec, p).map(CentreTypeListItemMapper::fromCec);
	}

	// Détail par identifiant.
	@Transactional(readOnly = true)
	public Optional<Cec> findById(Integer id) {
		return cecRepository.findById(id);
	}

	// Persistance avec contrôle du centre obligatoire.
	@Transactional
	public Cec save(Cec entity) {
		validateRequiredFields(entity);
		return cecRepository.save(entity);
	}

	// Crée un CEC lié à un centre existant.
	@Transactional
	public CentreTypeListItem create(SimpleCentreCreateRequest req) {
		if (req == null || req.getCentreId() == null) {
			throw new IllegalArgumentException("centreId est obligatoire");
		}
		Centre centre = centreRepository.findById(req.getCentreId())
				.orElseThrow(() -> new IllegalArgumentException("Centre introuvable: " + req.getCentreId()));
		Cec c = new Cec();
		c.setCentre(centre);
		c.setLibelleCec(req.getLibelle());
		copyCentreFieldsToCec(c, centre);
		return CentreTypeListItemMapper.fromCec(save(c));
	}

	// Crée promoteur, centre puis fiche CEC.
	@Transactional
	public CentreTypeListItem createFull(SimpleCentreTypeFullCreateRequest req) {
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
		entity.setLibelleCec(req.getLibelle());
		copyCentreFieldsToCec(entity, savedCentre);
		return CentreTypeListItemMapper.fromCec(save(entity));
	}

	// Met à jour le libellé uniquement.
	@Transactional
	public Optional<CentreTypeListItem> updateLibelle(Integer id, UpdateLibelleRequest req) {
		Optional<Cec> opt = findById(id);
		if (opt.isEmpty()) return Optional.empty();
		Cec existing = opt.get();
		existing.setLibelleCec(req == null ? null : req.getLibelle());
		return Optional.of(CentreTypeListItemMapper.fromCec(save(existing)));
	}

	// Met à jour les informations détaillées du CEC.
	@Transactional
	public Optional<CentreTypeListItem> updateInfos(Integer id, UpdateCentreTypeInfosRequest req) {
		Optional<Cec> opt = findById(id);
		if (opt.isEmpty()) return Optional.empty();
		Cec existing = opt.get();
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
			existing.setEncadreurNonMena(req.getEncadreurNonMena());
			existing.setEncadrerParMena(req.getEncadrerParMena());
		}
		return Optional.of(CentreTypeListItemMapper.fromCec(save(existing)));
	}

	// Supprime un CEC.
	@Transactional
	public void deleteById(Integer id) {
		cecRepository.deleteById(id);
	}

	// Contrôle minimal : centre présent.
	private void validateRequiredFields(Cec entity) {
		if (entity.getCentre() == null) {
			throw new IllegalArgumentException("Le centre est obligatoire pour un CEC.");
		}
	}

	// Alignement des champs dénormalisés depuis l’entité Centre.
	private void copyCentreFieldsToCec(Cec cec, Centre centre) {
		cec.setIdLocalite(centre.getIdLocalite() != null ? centre.getIdLocalite().getId() : null);
		cec.setIdPeriodicite(centre.getIdPeriodicite() != null ? centre.getIdPeriodicite().getId() : null);
		cec.setIdIep(centre.getIdIep() != null ? centre.getIdIep().getId() : null);
		cec.setIdAutoriteAutorisation(centre.getIdAutoriteAutorisation() != null ? centre.getIdAutoriteAutorisation().getId() : null);
		cec.setIdNaturecentre(centre.getIdNaturecentre() != null ? centre.getIdNaturecentre().getId() : null);
		cec.setIdPromoteur(centre.getIdPromoteur() != null ? centre.getIdPromoteur().getId() : null);
		cec.setCodeCentre(centre.getCodeCentre());
		cec.setAutorisation(centre.getAutorisation());
		cec.setEncadreurNonMena(centre.getEncadreurNonMena());
		cec.setEncadrerParMena(centre.getEncadrerParMena());
		cec.setEstElectrifie(centre.getEstElectrifie());
		cec.setADeLeau(centre.getADeLeau());
		cec.setNombreVisite(centre.getNombreVisite());
		cec.setLocalisationCentre(centre.getLocalisationCentre());
		cec.setNomMilieuImplentation(centre.getNomMilieuImplentation());
	}
}
