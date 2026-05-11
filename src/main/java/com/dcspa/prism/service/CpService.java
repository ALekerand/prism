package com.dcspa.prism.service;

import com.dcspa.prism.dto.CentreCreatePayload;
import com.dcspa.prism.dto.CentreSearchRequest;
import com.dcspa.prism.dto.CentreWithPromoteurItem;
import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.CpListFilter;
import com.dcspa.prism.dto.SimpleCentreCreateRequest;
import com.dcspa.prism.dto.SimpleCentreTypeFullCreateRequest;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.dto.UpdateLibelleRequest;
import com.dcspa.prism.entity.AutoriteAutorisation;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.entity.Iep;
import com.dcspa.prism.entity.LocaliteDImplantation;
import com.dcspa.prism.entity.MilieuImplantation;
import com.dcspa.prism.entity.Naturecentre;
import com.dcspa.prism.entity.Periodicite;
import com.dcspa.prism.entity.Personnephysique;
import com.dcspa.prism.entity.Personnemorale;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.entity.TypePromoteur;
import com.dcspa.prism.repository.AutoriteAutorisationRepository;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.CpRepository;
import com.dcspa.prism.repository.IeppRepository;
import com.dcspa.prism.repository.LocaliteDImplantationRepository;
import com.dcspa.prism.repository.MilieuImplantationRepository;
import com.dcspa.prism.repository.NaturecentreRepository;
import com.dcspa.prism.repository.PeriodiciteRepository;
import com.dcspa.prism.repository.PersonnephysiqueRepository;
import com.dcspa.prism.repository.PersonnemoraleRepository;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CpService {

	private final CpRepository cpRepository;
	private final CentreRepository centreRepository;
	private final CentreService centreService;
	private final PromoteurUpsertService promoteurUpsertService;
	private final LocaliteDImplantationRepository localiteRepository;
	private final MilieuImplantationRepository milieuImplantationRepository;
	private final IeppRepository iepRepository;
	private final NaturecentreRepository naturecentreRepository;
	private final PeriodiciteRepository periodiciteRepository;
	private final AutoriteAutorisationRepository autoriteAutorisationRepository;
	private final PromoteurRepository promoteurRepository;
	private final PersonnephysiqueRepository personnephysiqueRepository;
	private final PersonnemoraleRepository personnemoraleRepository;

	// Charge tous les CP.
	@Transactional(readOnly = true)
	public List<Cp> findAll() {
		return cpRepository.findAll();
	}

	// Liste paginée avec filtres optionnels sur chaque colonne.
	@Transactional(readOnly = true)
	public Page<CentreTypeListItem> findAllListItems(Pageable pageable, CpListFilter filter) {
		Pageable p = PageableUtils.cap(pageable);
		Specification<Cp> spec = SimpleCentreTypeSpecifications.forCp(filter);
		return cpRepository.findAll(spec, p).map(CentreTypeListItemMapper::fromCp);
	}

	// Détail par identifiant.
	@Transactional(readOnly = true)
	public Optional<Cp> findById(Integer id) {
		return cpRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<CentreWithPromoteurItem> findDetailedById(Integer id) {
		return findById(id).map(this::toDetailedItem);
	}

	@Transactional(readOnly = true)
	public List<CentreWithPromoteurItem> searchDetailed(CentreSearchRequest request) {
		Map<String, String> criteria = request == null ? null : request.getCriteria();
		return cpRepository.findAll().stream()
				.map(this::toDetailedItem)
				.filter(item -> CentreDetailedSearchSupport.matchesCriteria(item, criteria))
				.toList();
	}

	// Persistance avec contrôle du centre obligatoire.
	@Transactional
	public Cp save(Cp entity) {
		validateRequiredFields(entity);
		return cpRepository.save(entity);
	}

	// Crée un CP lié à un centre existant.
	@Transactional
	public CentreTypeListItem create(SimpleCentreCreateRequest req) {
		if (req == null || req.getCentreId() == null) {
			throw new IllegalArgumentException("centreId est obligatoire");
		}
		Centre centre = centreRepository.findById(req.getCentreId())
				.orElseThrow(() -> new IllegalArgumentException("Centre introuvable: " + req.getCentreId()));
		Cp cp = new Cp();
		cp.setCentre(centre);
		cp.setLibellleCp(req.getLibelle());
		copyCentreFieldsToCp(cp, centre);
		return CentreTypeListItemMapper.fromCp(save(cp));
	}

	// Crée promoteur, centre puis fiche CP.
	@Transactional
	public CentreTypeListItem createFull(SimpleCentreTypeFullCreateRequest req) {
		if (req == null) throw new IllegalArgumentException("Requête obligatoire");
		if (req.getCentre() == null) throw new IllegalArgumentException("centre est obligatoire");
		if (req.getPromoteur() == null) throw new IllegalArgumentException("promoteur est obligatoire");

		CentreCreatePayload c = req.getCentre();
		Promoteur promoteur = promoteurUpsertService.resolveOrCreate(req.getPromoteur());

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
		centre.setNomMilieuImplentation(resolveNomMilieuImplentation(c.getIdMilieuImplentation(), localite));
		Centre savedCentre = centreService.save(centre);

		Cp entity = new Cp();
		entity.setCentre(savedCentre);
		entity.setLibellleCp(req.getLibelle());
		copyCentreFieldsToCp(entity, savedCentre);
		return CentreTypeListItemMapper.fromCp(save(entity));
	}

	// Met à jour le libellé uniquement.
	@Transactional
	public Optional<CentreTypeListItem> updateLibelle(Integer id, UpdateLibelleRequest req) {
		Optional<Cp> opt = findById(id);
		if (opt.isEmpty()) return Optional.empty();
		Cp existing = opt.get();
		existing.setLibellleCp(req == null ? null : req.getLibelle());
		return Optional.of(CentreTypeListItemMapper.fromCp(save(existing)));
	}

	// Met à jour les informations détaillées du CP.
	@Transactional
	public Optional<CentreTypeListItem> updateInfos(Integer id, UpdateCentreTypeInfosRequest req) {
		Optional<Cp> opt = findById(id);
		if (opt.isEmpty()) return Optional.empty();
		Cp existing = opt.get();
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
			existing.setNomMilieuImplentation(resolveNomMilieuImplentationForUpdate(req, existing.getIdLocalite()));
			existing.setEncadreurNonMena(req.getEncadreurNonMena());
			existing.setEncadrerParMena(req.getEncadrerParMena());
			CentrePromoteurSync.applyPromoteurChange(req, existing.getId(), centreRepository, promoteurRepository, existing::setIdPromoteur);
		}
		return Optional.of(CentreTypeListItemMapper.fromCp(save(existing)));
	}

	// Supprime un CP.
	@Transactional
	public void deleteById(Integer id) {
		cpRepository.deleteById(id);
	}

	// Contrôle minimal : centre présent.
	private void validateRequiredFields(Cp entity) {
		if (entity.getCentre() == null) {
			throw new IllegalArgumentException("Le centre est obligatoire pour un CP.");
		}
	}

	// Alignement des champs dénormalisés depuis l’entité Centre.
	private void copyCentreFieldsToCp(Cp cp, Centre centre) {
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
	}

	private CentreWithPromoteurItem toDetailedItem(Cp cp) {
		CentreWithPromoteurItem item = new CentreWithPromoteurItem();
		item.setIdCentre(cp.getId());
		item.setCodeCentre(cp.getCodeCentre());
		item.setLibelle(cp.getLibellleCp());
		item.setIdLocalite(cp.getIdLocalite());
		item.setIdIep(cp.getIdIep());
		item.setIdNaturecentre(cp.getIdNaturecentre());
		item.setIdPeriodicite(cp.getIdPeriodicite());
		item.setIdAutoriteAutorisation(cp.getIdAutoriteAutorisation());
		item.setAutorisation(cp.getAutorisation());
		item.setEstElectrifie(cp.getEstElectrifie());
		item.setADeLeau(cp.getADeLeau());
		item.setNombreVisite(cp.getNombreVisite());
		item.setLocalisationCentre(cp.getLocalisationCentre());
		item.setNomMilieuImplentation(cp.getNomMilieuImplentation());
		item.setEncadreurNonMena(cp.getEncadreurNonMena());
		item.setEncadrerParMena(cp.getEncadrerParMena());
		attachReferences(item, cp);
		attachPromoteur(item, cp.getIdPromoteur());
		return item;
	}

	private void attachReferences(CentreWithPromoteurItem item, Cp cp) {
		if (cp.getIdLocalite() != null) {
			localiteRepository.findById(cp.getIdLocalite()).ifPresent(localite -> {
				CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
				ref.setId(localite.getId());
				ref.setCode(localite.getCodeLocalite());
				ref.setLibelle(localite.getNomLocalite());
				item.setLocalite(ref);
			});
		}
		if (cp.getIdIep() != null) {
			iepRepository.findById(cp.getIdIep()).ifPresent(iep -> {
				CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
				ref.setId(iep.getId());
				ref.setCode(iep.getCodeIep());
				ref.setLibelle(iep.getNomIep());
				item.setIep(ref);
			});
		}
		if (cp.getIdNaturecentre() != null) {
			naturecentreRepository.findById(cp.getIdNaturecentre()).ifPresent(nature -> {
				CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
				ref.setId(nature.getId());
				ref.setCode(nature.getCodeNatureCentre());
				ref.setLibelle(nature.getLibelleNatureCentre());
				item.setNaturecentre(ref);
			});
		}
		if (cp.getIdPeriodicite() != null) {
			periodiciteRepository.findById(cp.getIdPeriodicite().longValue()).ifPresent(periodicite -> {
				CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
				ref.setId(periodicite.getId().intValue());
				ref.setCode(periodicite.getCodePeriodicite());
				ref.setLibelle(periodicite.getLibellePeriodicite());
				item.setPeriodicite(ref);
			});
		}
		if (cp.getIdAutoriteAutorisation() != null) {
			autoriteAutorisationRepository.findById(cp.getIdAutoriteAutorisation()).ifPresent(autorite -> {
				CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
				ref.setId(autorite.getId());
				ref.setCode(autorite.getCodeAutorisation());
				ref.setLibelle(autorite.getLibelleAutoriteAutorisation());
				item.setAutoriteAutorisation(ref);
			});
		}
	}

	private String resolveNomMilieuImplentation(Integer milieuId, LocaliteDImplantation localite) {
		if (milieuId != null) {
			MilieuImplantation milieu = milieuImplantationRepository.findById(milieuId)
					.orElseThrow(() -> new IllegalArgumentException("Milieu implantation introuvable: " + milieuId));
			return milieu.getLibelleTypeImplentation();
		}
		if (localite != null && localite.getIdMilieuImplentation() != null) {
			return localite.getIdMilieuImplentation().getLibelleTypeImplentation();
		}
		return null;
	}

	private String resolveNomMilieuImplentationForUpdate(UpdateCentreTypeInfosRequest req, Integer currentLocaliteId) {
		Integer localiteId = req.getIdLocalite() != null ? req.getIdLocalite() : currentLocaliteId;
		LocaliteDImplantation localite = null;
		if (localiteId != null) {
			localite = localiteRepository.findById(localiteId).orElse(null);
		}
		return resolveNomMilieuImplentation(req.getIdMilieuImplentation(), localite);
	}

	private void attachPromoteur(CentreWithPromoteurItem item, Integer promoteurId) {
		if (promoteurId == null) return;
		Promoteur promoteur = promoteurRepository.findById(promoteurId).orElse(null);
		if (promoteur == null) return;

		CentreWithPromoteurItem.PromoteurDetails details = new CentreWithPromoteurItem.PromoteurDetails();
		details.setIdPromoteur(promoteur.getId());
		details.setCodePromoteur(promoteur.getCodePromoteur());
		details.setLibellePromoteur(promoteur.getLibellePromoteur());
		details.setTypePromoteur(promoteur.getTypePromoteur());

		Personnephysique pp = personnephysiqueRepository.findById(promoteurId).orElse(null);
		if (pp != null) {
			CentreWithPromoteurItem.PersonnePhysiqueDetails p = new CentreWithPromoteurItem.PersonnePhysiqueDetails();
			p.setLibellePersonnePhysique(pp.getLibellePersonnePhysique());
			p.setNom(pp.getNom());
			p.setPrenom(pp.getPrenom());
			p.setContact(pp.getContact());
			p.setFonction(pp.getFonction());
			details.setPersonnePhysique(p);
		}

		Personnemorale pm = personnemoraleRepository.findById(promoteurId).orElse(null);
		if (pm != null) {
			CentreWithPromoteurItem.PersonneMoraleDetails m = new CentreWithPromoteurItem.PersonneMoraleDetails();
			m.setDenomination(pm.getDenomination());
			m.setNomProgramme(pm.getNomProgramme());
			m.setNomRepresentant(pm.getNomRepresentantLegalStructure());
			m.setContact(pm.getContact());
			m.setBoitePostale(pm.getBoitePostale());
			m.setMail(pm.getMail());
			m.setIdTypePersonneMorale(pm.getTypePersonneMorale() != null ? pm.getTypePersonneMorale().getId() : null);
			m.setLibelleTypePersonneMorale(pm.getTypePersonneMorale() != null ? pm.getTypePersonneMorale().getLibelle() : null);
			details.setPersonneMorale(m);
		}
		if (details.getTypePromoteur() == null) {
			if (details.getPersonneMorale() != null) {
				details.setTypePromoteur(TypePromoteur.MORALE);
			} else if (details.getPersonnePhysique() != null) {
				details.setTypePromoteur(TypePromoteur.PHYSIQUE);
			}
		}
		item.setPromoteur(details);
	}

}
