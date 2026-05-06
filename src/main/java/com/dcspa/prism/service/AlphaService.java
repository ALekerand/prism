package com.dcspa.prism.service;

import com.dcspa.prism.dto.AlphaCreateRequest;
import com.dcspa.prism.dto.AlphaFullCreateRequest;
import com.dcspa.prism.dto.AlphaListFilter;
import com.dcspa.prism.dto.CentreCreatePayload;
import com.dcspa.prism.dto.CentreSearchRequest;
import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.CentreWithPromoteurItem;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.dto.UpdateLibelleRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.AutoriteAutorisation;
import com.dcspa.prism.entity.Campagne;
import com.dcspa.prism.entity.CategorieCentreAlpha;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Iep;
import com.dcspa.prism.entity.LocaliteDImplantation;
import com.dcspa.prism.entity.MilieuImplantation;
import com.dcspa.prism.entity.Naturecentre;
import com.dcspa.prism.entity.Periodicite;
import com.dcspa.prism.entity.Personnephysique;
import com.dcspa.prism.entity.Personnemorale;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.entity.Regimealphabetisation;
import com.dcspa.prism.entity.TypeAlpha;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.AutoriteAutorisationRepository;
import com.dcspa.prism.repository.CampagneRepository;
import com.dcspa.prism.repository.CategorieCentreAlphaRepository;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.IeppRepository;
import com.dcspa.prism.repository.LocaliteDImplantationRepository;
import com.dcspa.prism.repository.MilieuImplantationRepository;
import com.dcspa.prism.repository.NaturecentreRepository;
import com.dcspa.prism.repository.PeriodiciteRepository;
import com.dcspa.prism.repository.PersonnephysiqueRepository;
import com.dcspa.prism.repository.PersonnemoraleRepository;
import com.dcspa.prism.repository.PromoteurRepository;
import com.dcspa.prism.repository.RegimealphabetisationRepository;
import com.dcspa.prism.repository.TypeAlphaRepository;
import com.dcspa.prism.repository.spec.AlphaSpecifications;
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
public class AlphaService {

	private final AlphaRepository alphaRepository;
	private final CentreRepository centreRepository;
	private final CentreService centreService;
	private final CampagneRepository campagneRepository;
	private final CategorieCentreAlphaRepository categorieCentreAlphaRepository;
	private final TypeAlphaRepository typeAlphaRepository;
	private final RegimealphabetisationRepository regimealphabetisationRepository;
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

	// Charge toutes les entités Alpha depuis la base.
	@Transactional(readOnly = true)
	public List<Alpha> findAll() {
		return alphaRepository.findAll();
	}

	// Liste paginée pour l’API avec filtres optionnels sur chaque colonne.
	@Transactional(readOnly = true)
	public Page<CentreTypeListItem> findAllListItems(Pageable pageable, AlphaListFilter filter) {
		Pageable p = PageableUtils.cap(pageable);
		Specification<Alpha> spec = AlphaSpecifications.fromFilter(filter);
		return alphaRepository.findAll(spec, p).map(CentreTypeListItemMapper::fromAlpha);
	}

	// Recherche un Alpha par clé primaire.
	@Transactional(readOnly = true)
	public Optional<Alpha> findById(Integer id) {
		return alphaRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<CentreWithPromoteurItem> findDetailedById(Integer id) {
		return findById(id).map(this::toDetailedItem);
	}

	@Transactional(readOnly = true)
	public List<CentreWithPromoteurItem> searchDetailed(CentreSearchRequest request) {
		Map<String, String> criteria = request == null ? null : request.getCriteria();
		return alphaRepository.findAll().stream()
				.map(this::toDetailedItem)
				.filter(item -> CentreDetailedSearchSupport.matchesCriteria(item, criteria))
				.toList();
	}

	// Enregistre après validation des champs obligatoires.
	@Transactional
	public Alpha save(Alpha alpha) {
		validateRequiredFields(alpha);
		return alphaRepository.save(alpha);
	}

	// Crée un Alpha à partir d’un centre existant et des références (campagne, type, régime).
	@Transactional
	public CentreTypeListItem create(AlphaCreateRequest req) {
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
		alpha.setLibelleAlpha(req.getLibelleAlpha());
		copyCentreFieldsToAlpha(alpha, centre);
		return CentreTypeListItemMapper.fromAlpha(save(alpha));
	}

	// Chaîne promoteur → centre → Alpha puis retourne le DTO liste.
	@Transactional
	public CentreTypeListItem createFull(AlphaFullCreateRequest req) {
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
		alpha.setLibelleAlpha(req.getLibelleAlpha());
		copyCentreFieldsToAlpha(alpha, savedCentre);
		return CentreTypeListItemMapper.fromAlpha(save(alpha));
	}

	// Met à jour le libellé ; vide si l’identifiant n’existe pas.
	@Transactional
	public Optional<CentreTypeListItem> updateLibelle(Integer id, UpdateLibelleRequest req) {
		Optional<Alpha> opt = findById(id);
		if (opt.isEmpty()) return Optional.empty();
		Alpha existing = opt.get();
		existing.setLibelleAlpha(req == null ? null : req.getLibelle());
		return Optional.of(CentreTypeListItemMapper.fromAlpha(save(existing)));
	}

	// Met à jour les champs détaillés ; vide si l’identifiant n’existe pas.
	@Transactional
	public Optional<CentreTypeListItem> updateInfos(Integer id, UpdateCentreTypeInfosRequest req) {
		Optional<Alpha> opt = findById(id);
		if (opt.isEmpty()) return Optional.empty();
		Alpha existing = opt.get();
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
			existing.setNomMilieuImplentation(resolveNomMilieuImplentationForUpdate(req, existing.getIdLocalite()));
			existing.setEncadreurNonMena(req.getEncadreurNonMena());
			existing.setEncadrerParMena(req.getEncadrerParMena());
		}
		return Optional.of(CentreTypeListItemMapper.fromAlpha(save(existing)));
	}

	// Supprime un Alpha par identifiant.
	@Transactional
	public void deleteById(Integer id) {
		alphaRepository.deleteById(id);
	}

	// Vérifie les associations minimales avant persistance.
	private void validateRequiredFields(Alpha alpha) {
		if (alpha.getCentre() == null) {
			throw new IllegalArgumentException("Le centre est obligatoire pour une Alpha.");
		}
		if (alpha.getIdCompagne() == null) {
			throw new IllegalArgumentException("La campagne est obligatoire pour une Alpha.");
		}
		if (alpha.getIdTypeAlpha() == null) {
			throw new IllegalArgumentException("Le type d'alpha est obligatoire.");
		}
		if (alpha.getIdRegimeAlpha() == null) {
			throw new IllegalArgumentException("Le régime d'alphabétisation est obligatoire.");
		}
	}

	// Recopie les attributs « centre » sur la ligne Alpha (dénormalisation métier).
	private void copyCentreFieldsToAlpha(Alpha alpha, Centre centre) {
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
	}

	private CentreWithPromoteurItem toDetailedItem(Alpha alpha) {
		CentreWithPromoteurItem item = new CentreWithPromoteurItem();
		item.setIdCentre(alpha.getId());
		item.setCodeCentre(alpha.getCodeCentre());
		item.setCodeType(alpha.getCodeAlpha());
		item.setLibelle(alpha.getLibelleAlpha());
		item.setIdLocalite(alpha.getIdLocalite());
		item.setIdIep(alpha.getIdIep());
		item.setIdNaturecentre(alpha.getIdNaturecentre());
		item.setIdPeriodicite(alpha.getIdPeriodicite());
		item.setIdAutoriteAutorisation(alpha.getIdAutoriteAutorisation());
		item.setAutorisation(alpha.getAutorisation());
		item.setEstElectrifie(alpha.getEstElectrifie());
		item.setADeLeau(alpha.getADeLeau());
		item.setNombreVisite(alpha.getNombreVisite());
		item.setLocalisationCentre(alpha.getLocalisationCentre());
		item.setNomMilieuImplentation(alpha.getNomMilieuImplentation());
		item.setEncadreurNonMena(alpha.getEncadreurNonMena());
		item.setEncadrerParMena(alpha.getEncadrerParMena());
		attachReferences(item, alpha);
		attachPromoteur(item, alpha.getIdPromoteur());
		return item;
	}

	private void attachReferences(CentreWithPromoteurItem item, Alpha alpha) {
		if (alpha.getIdLocalite() != null) {
			localiteRepository.findById(alpha.getIdLocalite()).ifPresent(localite -> {
				CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
				ref.setId(localite.getId());
				ref.setCode(localite.getCodeLocalite());
				ref.setLibelle(localite.getNomLocalite());
				item.setLocalite(ref);
			});
		}
		if (alpha.getIdIep() != null) {
			iepRepository.findById(alpha.getIdIep()).ifPresent(iep -> {
				CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
				ref.setId(iep.getId());
				ref.setCode(iep.getCodeIep());
				ref.setLibelle(iep.getNomIep());
				item.setIep(ref);
			});
		}
		if (alpha.getIdNaturecentre() != null) {
			naturecentreRepository.findById(alpha.getIdNaturecentre()).ifPresent(nature -> {
				CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
				ref.setId(nature.getId());
				ref.setCode(nature.getCodeNatureCentre());
				ref.setLibelle(nature.getLibelleNatureCentre());
				item.setNaturecentre(ref);
			});
		}
		if (alpha.getIdPeriodicite() != null) {
			periodiciteRepository.findById(alpha.getIdPeriodicite().longValue()).ifPresent(periodicite -> {
				CentreWithPromoteurItem.ReferenceDetails ref = new CentreWithPromoteurItem.ReferenceDetails();
				ref.setId(periodicite.getId().intValue());
				ref.setCode(periodicite.getCodePeriodicite());
				ref.setLibelle(periodicite.getLibellePeriodicite());
				item.setPeriodicite(ref);
			});
		}
		if (alpha.getIdAutoriteAutorisation() != null) {
			autoriteAutorisationRepository.findById(alpha.getIdAutoriteAutorisation()).ifPresent(autorite -> {
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
		item.setPromoteur(details);
	}

}
