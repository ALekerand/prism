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
import com.dcspa.prism.entity.AnneScolaire;
import com.dcspa.prism.entity.AutoriteAutorisation;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Commune;
import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.entity.CpNiveau;
import com.dcspa.prism.entity.Departement;
import com.dcspa.prism.entity.Drena;
import com.dcspa.prism.entity.Iep;
import com.dcspa.prism.entity.LocaliteDImplantation;
import com.dcspa.prism.entity.MilieuImplantation;
import com.dcspa.prism.entity.Naturecentre;
import com.dcspa.prism.entity.Periodicite;
import com.dcspa.prism.entity.Personnephysique;
import com.dcspa.prism.entity.Personnemorale;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.entity.Region;
import com.dcspa.prism.entity.SousPrefecture;
import com.dcspa.prism.entity.TypePromoteur;
import com.dcspa.prism.repository.AutoriteAutorisationRepository;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.CpNiveauRepository;
import com.dcspa.prism.repository.CpRepository;
import com.dcspa.prism.repository.IeppRepository;
import com.dcspa.prism.repository.LocaliteDImplantationRepository;
import com.dcspa.prism.repository.MilieuImplantationRepository;
import com.dcspa.prism.repository.NaturecentreRepository;
import com.dcspa.prism.repository.PeriodiciteRepository;
import com.dcspa.prism.repository.PersonnephysiqueRepository;
import com.dcspa.prism.repository.PersonnemoraleRepository;
import com.dcspa.prism.repository.PromoteurRepository;
import com.dcspa.prism.repository.spec.CentreCirconscriptionSpecifications;
import com.dcspa.prism.repository.spec.SimpleCentreTypeSpecifications;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.pagination.PageableUtils;
import com.dcspa.prism.support.NumericSanitizer;
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
	private final CentreNiveauCreationService centreNiveauCreationService;
	private final CpNiveauRepository cpNiveauRepository;
	private final CirconscriptionResolver circonscriptionResolver;
	private final CirconscriptionWriteGuard circonscriptionWriteGuard;

	// Charge tous les CP.
	@Transactional(readOnly = true)
	public List<Cp> findAll() {
		return cpRepository.findAll();
	}

	// Liste paginée avec filtres optionnels sur chaque colonne.
	@Transactional(readOnly = true)
	public Page<CentreTypeListItem> findAllListItems(Pageable pageable, CpListFilter filter, AuthUser authUser) {
		Pageable p = PageableUtils.cap(pageable);
		Specification<Cp> spec = combineCpScope(filter, authUser);
		return cpRepository.findAll(spec, p).map(CentreTypeListItemMapper::fromCp);
	}

	// Détail par identifiant.
	@Transactional(readOnly = true)
	public Optional<Cp> findById(Integer id) {
		return cpRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<CentreWithPromoteurItem> findDetailedById(Integer id, AuthUser authUser) {
		if (!isCpWithinCirconscription(id, authUser)) {
			return Optional.empty();
		}
		return findById(id).map(this::toDetailedItem);
	}

	@Transactional(readOnly = true)
	public List<CentreWithPromoteurItem> searchDetailed(CentreSearchRequest request, AuthUser authUser) {
		Map<String, String> criteria = request == null ? null : request.getCriteria();
		Specification<Cp> scope = CentreCirconscriptionSpecifications.forCp(circonscriptionResolver.resolve(authUser));
		List<Cp> source = scope == null ? cpRepository.findAll() : cpRepository.findAll(scope);
		return source.stream()
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
	public CentreTypeListItem create(SimpleCentreCreateRequest req, AuthUser authUser) {
		if (req == null || req.getCentreId() == null) {
			throw new IllegalArgumentException("centreId est obligatoire");
		}
		Centre centre = centreRepository.findById(req.getCentreId())
				.orElseThrow(() -> new IllegalArgumentException("Centre introuvable: " + req.getCentreId()));
		circonscriptionWriteGuard.assertCentreEntityMatchesUser(centre, authUser);
		Cp cp = new Cp();
		cp.setCentre(centre);
		cp.setLibellleCp(req.getLibelle());
		copyCentreFieldsToCp(cp, centre);
		return CentreTypeListItemMapper.fromCp(save(cp));
	}

	// Crée promoteur, centre puis fiche CP.
	@Transactional
	public CentreTypeListItem createFull(SimpleCentreTypeFullCreateRequest req, AuthUser authUser) {
		if (req == null) throw new IllegalArgumentException("Requête obligatoire");
		if (req.getCentre() == null) throw new IllegalArgumentException("centre est obligatoire");
		if (req.getPromoteur() == null) throw new IllegalArgumentException("promoteur est obligatoire");

		CentreCreatePayload c = req.getCentre();
		circonscriptionWriteGuard.prepareCentreCreatePayload(c, authUser);
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
		centre.setNombreVisite(NumericSanitizer.nonNegativeOrNull(c.getNombreVisite()));
		centre.setLocalisationCentre(c.getLocalisationCentre());
		centre.setNomMilieuImplentation(resolveNomMilieuImplentation(c.getIdMilieuImplentation(), localite));
		CentreSupplementalFields.applyToCentre(centre, c);
		Centre savedCentre = centreService.save(centre);

		Cp entity = new Cp();
		entity.setCentre(savedCentre);
		entity.setLibellleCp(req.getLibelle());
		copyCentreFieldsToCp(entity, savedCentre);
		Cp savedCp = save(entity);
		centreNiveauCreationService.createCpNiveaux(savedCp, req.getNiveaux());
		return CentreTypeListItemMapper.fromCp(savedCp);
	}

	// Met à jour le libellé uniquement.
	@Transactional
	public Optional<CentreTypeListItem> updateLibelle(Integer id, UpdateLibelleRequest req, AuthUser authUser) {
		if (!isCpWithinCirconscription(id, authUser)) {
			return Optional.empty();
		}
		Optional<Cp> opt = findById(id);
		if (opt.isEmpty()) return Optional.empty();
		Cp existing = opt.get();
		existing.setLibellleCp(req == null ? null : req.getLibelle());
		return Optional.of(CentreTypeListItemMapper.fromCp(save(existing)));
	}

	@Transactional
	public Optional<CentreTypeListItem> updateActif(Integer id, boolean actif, AuthUser authUser) {
		if (!isCpWithinCirconscription(id, authUser)) {
			return Optional.empty();
		}
		if (!cpRepository.existsById(id)) {
			return Optional.empty();
		}
		int updated = centreRepository.updateActifById(id, actif);
		if (updated == 0) {
			throw new IllegalArgumentException("Centre introuvable pour CP #" + id);
		}
		Cp existing = cpRepository.findById(id).orElse(null);
		if (existing == null) {
			return Optional.empty();
		}
		return Optional.of(CentreTypeListItemMapper.fromCp(existing, actif));
	}

	// Met à jour les informations détaillées du CP.
	@Transactional
	public Optional<CentreTypeListItem> updateInfos(Integer id, UpdateCentreTypeInfosRequest req, AuthUser authUser) {
		if (!isCpWithinCirconscription(id, authUser)) {
			return Optional.empty();
		}
		Optional<Cp> opt = findById(id);
		if (opt.isEmpty()) return Optional.empty();
		Cp existing = opt.get();
		if (req != null) {
			circonscriptionWriteGuard.sanitizeUpdateCentreInfos(req, authUser, existing.getIdIep(), existing.getIdLocalite());
			existing.setLibellleCp(req.getLibelle());
			existing.setIdLocalite(req.getIdLocalite());
			existing.setIdIep(req.getIdIep());
			existing.setIdNaturecentre(req.getIdNaturecentre());
			existing.setIdPeriodicite(req.getIdPeriodicite());
			existing.setIdAutoriteAutorisation(req.getIdAutoriteAutorisation());
			existing.setAutorisation(req.getAutorisation());
			existing.setEstElectrifie(req.getEstElectrifie());
			existing.setADeLeau(req.getADeLeau());
			existing.setNombreVisite(NumericSanitizer.nonNegativeOrNull(req.getNombreVisite()));
			existing.setLocalisationCentre(req.getLocalisationCentre());
			existing.setNomMilieuImplentation(resolveNomMilieuImplentationForUpdate(req, existing.getIdLocalite()));
			existing.setEncadreurNonMena(req.getEncadreurNonMena());
			existing.setEncadrerParMena(req.getEncadrerParMena());
			CentreSupplementalFields.applyUpdate(existing, req);
			CentrePromoteurSync.applyPromoteurChange(req, existing.getId(), centreRepository, promoteurRepository, existing::setIdPromoteur);
			if (req.getNiveaux() != null) {
				centreNiveauCreationService.replaceCpNiveaux(existing, req.getNiveaux());
			}
			syncCentreActif(existing.getId(), req);
		}
		Cp saved = save(existing);
		centreRepository.findById(saved.getId()).ifPresent(saved::setCentre);
		return Optional.of(CentreTypeListItemMapper.fromCp(saved));
	}

	// Supprime un CP.
	@Transactional
	public boolean deleteById(Integer id, AuthUser authUser) {
		if (!isCpWithinCirconscription(id, authUser)) {
			return false;
		}
		cpRepository.deleteById(id);
		return true;
	}

	private Specification<Cp> combineCpScope(CpListFilter filter, AuthUser authUser) {
		Specification<Cp> base = SimpleCentreTypeSpecifications.forCp(filter);
		Specification<Cp> scope = CentreCirconscriptionSpecifications.forCp(circonscriptionResolver.resolve(authUser));
		if (scope == null) {
			return base;
		}
		return Specification.where(scope).and(base);
	}

	private boolean isCpWithinCirconscription(Integer cpId, AuthUser user) {
		if (cpId == null) {
			return false;
		}
		Specification<Cp> scope = CentreCirconscriptionSpecifications.forCp(circonscriptionResolver.resolve(user));
		if (scope == null) {
			return true;
		}
		Specification<Cp> idEq = (root, query, cb) -> cb.equal(root.get("id"), cpId);
		return cpRepository.count(Specification.where(scope).and(idEq)) > 0;
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
		CentreSupplementalFields.copyToCp(cp, centre);
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
		CentreSupplementalFields.fillItem(item, cp);
		item.setLocalisationCentre(cp.getLocalisationCentre());
		item.setNomMilieuImplentation(cp.getNomMilieuImplentation());
		item.setEncadreurNonMena(cp.getEncadreurNonMena());
		item.setEncadrerParMena(cp.getEncadrerParMena());
		attachReferences(item, cp);
		attachPromoteur(item, cp.getIdPromoteur());
		item.setNiveaux(cpNiveauRepository.findByIdCentre_Id(cp.getId()).stream()
				.map(this::toNiveauDetails)
				.toList());
		centreRepository.findById(cp.getId()).ifPresent(c -> item.setActif(CentreSupplementalFields.actifForApi(c)));
		return item;
	}

	private CentreWithPromoteurItem.NiveauDetails toNiveauDetails(CpNiveau niveau) {
		CentreWithPromoteurItem.NiveauDetails details = new CentreWithPromoteurItem.NiveauDetails();
		details.setId(niveau.getId());
		if (niveau.getIdNiveauCp() != null) {
			details.setNiveauId(niveau.getIdNiveauCp().getId());
			details.setLibelleNiveau(niveau.getIdNiveauCp().getLibelleNiveauCp());
		}
		details.setAnneeScolaire(toAnneeScolaireRef(niveau.getIdAnneeScolaire()));
		details.setNombreSalle(niveau.getNombreSalleCp());
		return details;
	}

	private CentreWithPromoteurItem.ReferenceDetails toAnneeScolaireRef(AnneScolaire annee) {
		if (annee == null) return null;
		CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
		ref.setId(annee.getId());
		ref.setCode(annee.getCodeAnneeScolaire());
		if (annee.getDebutAnneeScolaire() != null && annee.getFinAnneeScolaire() != null) {
			ref.setLibelle(annee.getDebutAnneeScolaire() + " → " + annee.getFinAnneeScolaire());
		}
		return ref;
	}

	private void attachReferences(CentreWithPromoteurItem item, Cp cp) {
		if (cp.getIdLocalite() != null) {
			localiteRepository.findById(cp.getIdLocalite()).ifPresent(localite -> {
				item.setLocalite(toLocaliteRef(localite));
				attachLocaliteGeography(item, localite);
			});
		}
		if (cp.getIdIep() != null) {
			iepRepository.findById(cp.getIdIep()).ifPresent(iep -> {
				item.setIep(toIepRef(iep));
				item.setDrena(toDrenaRef(iep.getIdDrena()));
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

	private void attachLocaliteGeography(CentreWithPromoteurItem item, LocaliteDImplantation localite) {
		Commune commune = localite.getIdCommune();
		if (commune != null) {
			item.setCommune(toCommuneRef(commune));
		}
		SousPrefecture sousPrefecture = localite.getIdSousPrefecture();
		if (sousPrefecture != null) {
			item.setSousPrefecture(toSousPrefectureRef(sousPrefecture));
			Departement departement = sousPrefecture.getIdDepartement();
			if (departement != null) {
				item.setDepartement(toDepartementRef(departement));
				item.setRegion(toRegionRef(departement.getIdRegion()));
			}
		}
	}

	private CentreWithPromoteurItem.ReferenceDetails toLocaliteRef(LocaliteDImplantation localite) {
		CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
		ref.setId(localite.getId());
		ref.setCode(localite.getCodeLocalite());
		ref.setLibelle(localite.getNomLocalite());
		return ref;
	}

	private CentreWithPromoteurItem.ReferenceDetails toIepRef(Iep iep) {
		CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
		ref.setId(iep.getId());
		ref.setCode(iep.getCodeIep());
		ref.setLibelle(iep.getNomIep());
		return ref;
	}

	private CentreWithPromoteurItem.ReferenceDetails toDrenaRef(Drena drena) {
		if (drena == null) return null;
		CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
		ref.setId(drena.getId());
		ref.setCode(drena.getCodeDrena());
		ref.setLibelle(drena.getNomDrena());
		return ref;
	}

	private CentreWithPromoteurItem.ReferenceDetails toCommuneRef(Commune commune) {
		CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
		ref.setId(commune.getId());
		ref.setCode(commune.getCodeCommune());
		ref.setLibelle(commune.getNomCommune());
		return ref;
	}

	private CentreWithPromoteurItem.ReferenceDetails toSousPrefectureRef(SousPrefecture sousPrefecture) {
		CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
		ref.setId(sousPrefecture.getId());
		ref.setCode(sousPrefecture.getCodeSousPrefecture());
		ref.setLibelle(sousPrefecture.getNomSousPrefecture());
		return ref;
	}

	private CentreWithPromoteurItem.ReferenceDetails toDepartementRef(Departement departement) {
		CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
		ref.setId(departement.getId());
		ref.setCode(departement.getCodeDepartement());
		ref.setLibelle(departement.getNomDepartement());
		return ref;
	}

	private CentreWithPromoteurItem.ReferenceDetails toRegionRef(Region region) {
		if (region == null) return null;
		CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
		ref.setId(region.getId());
		ref.setCode(region.getCodeRegion());
		ref.setLibelle(region.getLibelleRegion());
		return ref;
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
			p.setSexe(pp.getSexe());
			p.setDateNaissance(pp.getDateNaissance());
			p.setAnciennete(pp.getAnciennete());
			p.setBoitePostale(pp.getBoitePostale());
			p.setNiveauEtudes(pp.getNiveauEtudes());
			p.setCivilite(pp.getCivilite());
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

	private void syncCentreActif(Integer centreId, UpdateCentreTypeInfosRequest req) {
		if (req == null || req.getActif() == null || centreId == null) {
			return;
		}
		centreRepository.findById(centreId).ifPresent(c -> {
			CentreSupplementalFields.applyActifToCentre(c, req.getActif());
			centreRepository.save(c);
		});
	}

}
