package com.dcspa.prism.service;

import com.dcspa.prism.dto.CecListFilter;
import com.dcspa.prism.dto.CentreCreatePayload;
import com.dcspa.prism.dto.CentreSearchRequest;
import com.dcspa.prism.dto.CentreWithPromoteurItem;
import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.SimpleCentreCreateRequest;
import com.dcspa.prism.dto.SimpleCentreTypeFullCreateRequest;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.dto.UpdateLibelleRequest;
import com.dcspa.prism.entity.AnneScolaire;
import com.dcspa.prism.entity.AutoriteAutorisation;
import com.dcspa.prism.entity.Cec;
import com.dcspa.prism.entity.CecNiveau;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Commune;
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
import com.dcspa.prism.repository.AutoriteAutorisationRepository;
import com.dcspa.prism.repository.CecNiveauRepository;
import com.dcspa.prism.repository.CecRepository;
import com.dcspa.prism.repository.CentreRepository;
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
public class CecService {

	private final CecRepository cecRepository;
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
	private final CecNiveauRepository cecNiveauRepository;
	private final CirconscriptionResolver circonscriptionResolver;
	private final CirconscriptionWriteGuard circonscriptionWriteGuard;

	// Charge tous les CEC.
	@Transactional(readOnly = true)
	public List<Cec> findAll() {
		return cecRepository.findAll();
	}

	// Liste paginée avec filtres optionnels sur chaque colonne.
	@Transactional(readOnly = true)
	public Page<CentreTypeListItem> findAllListItems(Pageable pageable, CecListFilter filter, AuthUser authUser) {
		Pageable p = PageableUtils.cap(pageable);
		Specification<Cec> spec = combineCecScope(filter, authUser);
		return cecRepository.findAll(spec, p).map(CentreTypeListItemMapper::fromCec);
	}

	// Détail par identifiant.
	@Transactional(readOnly = true)
	public Optional<Cec> findById(Integer id) {
		return cecRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<CentreWithPromoteurItem> findDetailedById(Integer id, AuthUser authUser) {
		if (!isCecWithinCirconscription(id, authUser)) {
			return Optional.empty();
		}
		return findById(id).map(this::toDetailedItem);
	}

	@Transactional(readOnly = true)
	public List<CentreWithPromoteurItem> searchDetailed(CentreSearchRequest request, AuthUser authUser) {
		Map<String, String> criteria = request == null ? null : request.getCriteria();
		Specification<Cec> scope = CentreCirconscriptionSpecifications.forCec(circonscriptionResolver.resolve(authUser));
		List<Cec> source = scope == null ? cecRepository.findAll() : cecRepository.findAll(scope);
		return source.stream()
				.map(this::toDetailedItem)
				.filter(item -> CentreDetailedSearchSupport.matchesCriteria(item, criteria))
				.toList();
	}

	// Persistance avec contrôle du centre obligatoire.
	@Transactional
	public Cec save(Cec entity) {
		validateRequiredFields(entity);
		return cecRepository.save(entity);
	}

	// Crée un CEC lié à un centre existant.
	@Transactional
	public CentreTypeListItem create(SimpleCentreCreateRequest req, AuthUser authUser) {
		if (req == null || req.getCentreId() == null) {
			throw new IllegalArgumentException("centreId est obligatoire");
		}
		Centre centre = centreRepository.findById(req.getCentreId())
				.orElseThrow(() -> new IllegalArgumentException("Centre introuvable: " + req.getCentreId()));
		circonscriptionWriteGuard.assertCentreEntityMatchesUser(centre, authUser);
		Cec c = new Cec();
		c.setCentre(centre);
		c.setLibelleCec(req.getLibelle());
		copyCentreFieldsToCec(c, centre);
		return CentreTypeListItemMapper.fromCec(save(c));
	}

	// Crée promoteur, centre puis fiche CEC.
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

		Cec entity = new Cec();
		entity.setCentre(savedCentre);
		entity.setLibelleCec(req.getLibelle());
		entity.setEcoleTutrice(trimToNull(req.getEcoleTutrice()));
		entity.setAnneeCreation(sanitizeAnneeCreation(req.getAnneeCreation()));
		copyCentreFieldsToCec(entity, savedCentre);
		Cec savedCec = save(entity);
		centreNiveauCreationService.createCecNiveaux(savedCec, req.getNiveaux());
		return CentreTypeListItemMapper.fromCec(savedCec);
	}

	// Met à jour le libellé uniquement.
	@Transactional
	public Optional<CentreTypeListItem> updateLibelle(Integer id, UpdateLibelleRequest req, AuthUser authUser) {
		if (!isCecWithinCirconscription(id, authUser)) {
			return Optional.empty();
		}
		Optional<Cec> opt = findById(id);
		if (opt.isEmpty()) return Optional.empty();
		Cec existing = opt.get();
		existing.setLibelleCec(req == null ? null : req.getLibelle());
		return Optional.of(CentreTypeListItemMapper.fromCec(save(existing)));
	}

	@Transactional
	public Optional<CentreTypeListItem> updateActif(Integer id, boolean actif, AuthUser authUser) {
		if (!isCecWithinCirconscription(id, authUser)) {
			return Optional.empty();
		}
		if (!cecRepository.existsById(id)) {
			return Optional.empty();
		}
		int updated = centreRepository.updateActifById(id, actif);
		if (updated == 0) {
			throw new IllegalArgumentException("Centre introuvable pour CEC #" + id);
		}
		Cec existing = cecRepository.findById(id).orElse(null);
		if (existing == null) {
			return Optional.empty();
		}
		return Optional.of(CentreTypeListItemMapper.fromCec(existing, actif));
	}

	// Met à jour les informations détaillées du CEC.
	@Transactional
	public Optional<CentreTypeListItem> updateInfos(Integer id, UpdateCentreTypeInfosRequest req, AuthUser authUser) {
		if (!isCecWithinCirconscription(id, authUser)) {
			return Optional.empty();
		}
		Optional<Cec> opt = findById(id);
		if (opt.isEmpty()) return Optional.empty();
		Cec existing = opt.get();
		if (req != null) {
			circonscriptionWriteGuard.sanitizeUpdateCentreInfos(req, authUser, existing.getIdIep(), existing.getIdLocalite());
			existing.setLibelleCec(req.getLibelle());
			existing.setIdLocalite(req.getIdLocalite());
			existing.setIdIep(req.getIdIep());
			existing.setIdNaturecentre(req.getIdNaturecentre());
			existing.setIdPeriodicite(req.getIdPeriodicite());
			existing.setIdAutoriteAutorisation(req.getIdAutoriteAutorisation());
			existing.setAutorisation(req.getAutorisation());
			existing.setEstElectrifie(req.getEstElectrifie());
			existing.setADeLeau(req.getADeLeau());
			existing.setNombreVisite(NumericSanitizer.nonNegativeOrNull(req.getNombreVisite()));
			existing.setEcoleTutrice(trimToNull(req.getEcoleTutrice()));
			existing.setAnneeCreation(sanitizeAnneeCreation(req.getAnneeCreation()));
			existing.setLocalisationCentre(req.getLocalisationCentre());
			existing.setNomMilieuImplentation(resolveNomMilieuImplentationForUpdate(req, existing.getIdLocalite()));
			existing.setEncadreurNonMena(req.getEncadreurNonMena());
			existing.setEncadrerParMena(req.getEncadrerParMena());
			CentreSupplementalFields.applyUpdate(existing, req);
			CentrePromoteurSync.applyPromoteurChange(req, existing.getId(), centreRepository, promoteurRepository, existing::setIdPromoteur);
			if (req.getNiveaux() != null) {
				centreNiveauCreationService.replaceCecNiveaux(existing, req.getNiveaux());
			}
			syncCentreActif(existing.getId(), req);
		}
		Cec saved = save(existing);
		centreRepository.findById(saved.getId()).ifPresent(saved::setCentre);
		return Optional.of(CentreTypeListItemMapper.fromCec(saved));
	}

	// Supprime un CEC.
	@Transactional
	public boolean deleteById(Integer id, AuthUser authUser) {
		if (!isCecWithinCirconscription(id, authUser)) {
			return false;
		}
		cecRepository.deleteById(id);
		return true;
	}

	private Specification<Cec> combineCecScope(CecListFilter filter, AuthUser authUser) {
		Specification<Cec> base = SimpleCentreTypeSpecifications.forCec(filter);
		Specification<Cec> scope = CentreCirconscriptionSpecifications.forCec(circonscriptionResolver.resolve(authUser));
		if (scope == null) {
			return base;
		}
		return Specification.where(scope).and(base);
	}

	private boolean isCecWithinCirconscription(Integer cecId, AuthUser user) {
		if (cecId == null) {
			return false;
		}
		Specification<Cec> scope = CentreCirconscriptionSpecifications.forCec(circonscriptionResolver.resolve(user));
		if (scope == null) {
			return true;
		}
		Specification<Cec> idEq = (root, query, cb) -> cb.equal(root.get("id"), cecId);
		return cecRepository.count(Specification.where(scope).and(idEq)) > 0;
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
		CentreSupplementalFields.copyToCec(cec, centre);
	}

	private CentreWithPromoteurItem toDetailedItem(Cec cec) {
		CentreWithPromoteurItem item = new CentreWithPromoteurItem();
		item.setIdCentre(cec.getId());
		item.setCodeCentre(cec.getCodeCentre());
		item.setLibelle(cec.getLibelleCec());
		item.setIdLocalite(cec.getIdLocalite());
		item.setIdIep(cec.getIdIep());
		item.setIdNaturecentre(cec.getIdNaturecentre());
		item.setIdPeriodicite(cec.getIdPeriodicite());
		item.setIdAutoriteAutorisation(cec.getIdAutoriteAutorisation());
		item.setAutorisation(cec.getAutorisation());
		item.setEstElectrifie(cec.getEstElectrifie());
		item.setADeLeau(cec.getADeLeau());
		item.setNombreVisite(cec.getNombreVisite());
		item.setEcoleTutrice(cec.getEcoleTutrice());
		item.setAnneeCreation(cec.getAnneeCreation());
		CentreSupplementalFields.fillItem(item, cec);
		item.setLocalisationCentre(cec.getLocalisationCentre());
		item.setNomMilieuImplentation(cec.getNomMilieuImplentation());
		item.setEncadreurNonMena(cec.getEncadreurNonMena());
		item.setEncadrerParMena(cec.getEncadrerParMena());
		attachReferences(item, cec);
		item.setNiveaux(cecNiveauRepository.findByIdCentre_Id(cec.getId()).stream()
				.map(this::toNiveauDetails)
				.toList());

		if (cec.getIdPromoteur() != null) {
			Promoteur promoteur = promoteurRepository.findById(cec.getIdPromoteur()).orElse(null);
			if (promoteur != null) {
				CentreWithPromoteurItem.PromoteurDetails details = new CentreWithPromoteurItem.PromoteurDetails();
				details.setIdPromoteur(promoteur.getId());
				details.setCodePromoteur(promoteur.getCodePromoteur());
				details.setLibellePromoteur(promoteur.getLibellePromoteur());
				details.setTypePromoteur(promoteur.getTypePromoteur());

				Personnephysique pp = personnephysiqueRepository.findById(promoteur.getId()).orElse(null);
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

				Personnemorale pm = personnemoraleRepository.findById(promoteur.getId()).orElse(null);
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
				item.setPromoteur(details);
			}
		}
		centreRepository.findById(cec.getId()).ifPresent(c -> item.setActif(CentreSupplementalFields.actifForApi(c)));
		return item;
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

	private CentreWithPromoteurItem.NiveauDetails toNiveauDetails(CecNiveau niveau) {
		CentreWithPromoteurItem.NiveauDetails details = new CentreWithPromoteurItem.NiveauDetails();
		details.setId(niveau.getId());
		if (niveau.getIdNiveauSie() != null) {
			details.setNiveauId(niveau.getIdNiveauSie().getId());
			details.setCodeNiveau(niveau.getIdNiveauSie().getCodeNiveauSie());
			details.setLibelleNiveau(niveau.getIdNiveauSie().getLibelleNiveauSie());
		}
		if (details.getCodeNiveau() == null) {
			details.setCodeNiveau(niveau.getCodeNiveauCec());
		}
		details.setAnneeScolaire(toAnneeScolaireRef(niveau.getIdAnneeScolaire()));
		details.setNombreSalle(niveau.getNombreSalleCec());
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

	private void attachReferences(CentreWithPromoteurItem item, Cec cec) {
		if (cec.getIdLocalite() != null) {
			localiteRepository.findById(cec.getIdLocalite()).ifPresent(localite -> {
				item.setLocalite(toLocaliteRef(localite));
				attachLocaliteGeography(item, localite);
			});
		}
		if (cec.getIdIep() != null) {
			iepRepository.findById(cec.getIdIep()).ifPresent(iep -> {
				item.setIep(toIepRef(iep));
				item.setDrena(toDrenaRef(iep.getIdDrena()));
			});
		}
		if (cec.getIdNaturecentre() != null) {
			naturecentreRepository.findById(cec.getIdNaturecentre()).ifPresent(nature -> {
				CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
				ref.setId(nature.getId());
				ref.setCode(nature.getCodeNatureCentre());
				ref.setLibelle(nature.getLibelleNatureCentre());
				item.setNaturecentre(ref);
			});
		}
		if (cec.getIdPeriodicite() != null) {
			periodiciteRepository.findById(cec.getIdPeriodicite().longValue()).ifPresent(periodicite -> {
				CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
				ref.setId(periodicite.getId().intValue());
				ref.setCode(periodicite.getCodePeriodicite());
				ref.setLibelle(periodicite.getLibellePeriodicite());
				item.setPeriodicite(ref);
			});
		}
		if (cec.getIdAutoriteAutorisation() != null) {
			autoriteAutorisationRepository.findById(cec.getIdAutoriteAutorisation()).ifPresent(autorite -> {
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

	private static String trimToNull(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}

	private static Integer sanitizeAnneeCreation(Integer year) {
		if (year == null) {
			return null;
		}
		if (year < 1800 || year > 2100) {
			return null;
		}
		return year;
	}

	private String resolveNomMilieuImplentationForUpdate(UpdateCentreTypeInfosRequest req, Integer currentLocaliteId) {
		Integer localiteId = req.getIdLocalite() != null ? req.getIdLocalite() : currentLocaliteId;
		LocaliteDImplantation localite = null;
		if (localiteId != null) {
			localite = localiteRepository.findById(localiteId).orElse(null);
		}
		return resolveNomMilieuImplentation(req.getIdMilieuImplentation(), localite);
	}

}
