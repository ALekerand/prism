package com.dcspa.prism.service;

import com.dcspa.prism.dto.CentreCreatePayload;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Departement;
import com.dcspa.prism.entity.Iep;
import com.dcspa.prism.entity.LocaliteDImplantation;
import com.dcspa.prism.entity.SousPrefecture;
import com.dcspa.prism.repository.DrenaDepartementRepository;
import com.dcspa.prism.repository.IeppRepository;
import com.dcspa.prism.repository.LocaliteDImplantationRepository;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.circonscription.CirconscriptionAttachement;
import com.dcspa.prism.service.circonscription.CirconscriptionLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Applique et valide les rattachements géographiques des centres lors des créations / mises à jour,
 * à partir du profil de l’utilisateur connecté.
 */
@Component
@RequiredArgsConstructor
public class CirconscriptionWriteGuard {

	private static final String[] NATIONAL_ROLES = {
			"ADMIN", "SUPER_ADMIN", "SUPER_ROOT", "SUPERVISEUR_AENF", "DIRECTEUR"
	};

	private final CirconscriptionResolver circonscriptionResolver;
	private final IeppRepository iepRepository;
	private final LocaliteDImplantationRepository localiteRepository;
	private final DrenaDepartementRepository drenaDepartementRepository;

	public void prepareCentreCreatePayload(CentreCreatePayload c, AuthUser user) {
		if (user == null || c == null || isNationalUser(user)) {
			return;
		}
		applyUserAnchorsToPayload(c, user);
		validatePayloadAgainstScope(c, user);
	}

	public void assertCentreEntityMatchesUser(Centre centre, AuthUser user) {
		if (user == null || centre == null || isNationalUser(user)) {
			return;
		}
		CentreCreatePayload p = new CentreCreatePayload();
		p.setIepId(centre.getIdIep() != null ? centre.getIdIep().getId() : null);
		p.setLocaliteId(centre.getIdLocalite() != null ? centre.getIdLocalite().getId() : null);
		validatePayloadAgainstScope(p, user);
	}

	public void sanitizeUpdateCentreInfos(UpdateCentreTypeInfosRequest req, AuthUser user, Integer currentIepId, Integer currentLocaliteId) {
		if (req == null || user == null || isNationalUser(user)) {
			return;
		}
		CirconscriptionAttachement att = circonscriptionResolver.resolve(user);
		if (att.level() == CirconscriptionLevel.NONE) {
			return;
		}
		int effIep = req.getIdIep() != null ? req.getIdIep() : (currentIepId != null ? currentIepId : -1);
		int effLoc = req.getIdLocalite() != null ? req.getIdLocalite() : (currentLocaliteId != null ? currentLocaliteId : -1);
		if (user.getIdIep() != null) {
			req.setIdIep(user.getIdIep());
			effIep = user.getIdIep();
		}
		if (user.getIdLocalite() != null) {
			req.setIdLocalite(user.getIdLocalite());
			effLoc = user.getIdLocalite();
		}
		if (effIep <= 0) {
			throw new IllegalArgumentException("IEP obligatoire pour ce profil.");
		}
		if (effLoc <= 0) {
			throw new IllegalArgumentException("Localité obligatoire pour ce profil.");
		}
		validateIepAndLocalitePair(effLoc, effIep, att);
	}

	private void applyUserAnchorsToPayload(CentreCreatePayload c, AuthUser user) {
		if (user.getIdIep() != null) {
			c.setIepId(user.getIdIep());
		}
		if (user.getIdLocalite() != null) {
			c.setLocaliteId(user.getIdLocalite());
		}
	}

	private void validatePayloadAgainstScope(CentreCreatePayload c, AuthUser user) {
		CirconscriptionAttachement att = circonscriptionResolver.resolve(user);
		if (att.level() == CirconscriptionLevel.NONE) {
			return;
		}
		Integer iepId = c.getIepId();
		Integer locId = c.getLocaliteId();
		if (iepId == null) {
			throw new IllegalArgumentException("iepId est obligatoire.");
		}
		if (locId == null) {
			throw new IllegalArgumentException("localiteId est obligatoire.");
		}
		validateIepAndLocalitePair(locId, iepId, att);
	}

	private void validateIepAndLocalitePair(int localiteId, int iepId, CirconscriptionAttachement att) {
		Iep iep = iepRepository.findById(iepId)
				.orElseThrow(() -> new IllegalArgumentException("IEP introuvable: " + iepId));
		Integer drenaOfIep = iep.getIdDrena() != null ? iep.getIdDrena().getId() : null;
		LocaliteDImplantation loc = localiteRepository.findById(localiteId)
				.orElseThrow(() -> new IllegalArgumentException("Localité introuvable: " + localiteId));
		Integer regionIdOfLoc = regionIdOfLocalite(loc);

		switch (att.level()) {
			case NONE -> {
			}
			case IEP -> {
				if (!Integer.valueOf(iepId).equals(att.scopeId())) {
					throw new IllegalArgumentException("L’IEP du centre doit correspondre à votre circonscription.");
				}
				assertLocaliteLinkedToDrena(localiteId, drenaOfIep);
			}
			case DRENA -> {
				if (drenaOfIep == null || !drenaOfIep.equals(att.scopeId())) {
					throw new IllegalArgumentException("L’IEP doit relever de votre DRENA.");
				}
				assertLocaliteLinkedToDrena(localiteId, att.scopeId());
			}
			case REGION -> {
				if (regionIdOfLoc == null || !regionIdOfLoc.equals(att.scopeId())) {
					throw new IllegalArgumentException("La localité doit se situer dans votre région.");
				}
				assertLocaliteLinkedToDrena(localiteId, drenaOfIep);
			}
			default -> throw new IllegalStateException("Niveau de circonscription inattendu: " + att.level());
		}
	}

	private void assertLocaliteLinkedToDrena(LocaliteDImplantation loc, Integer drenaId) {
		Integer deptId = departementIdOfLocalite(loc);
		if (deptId == null) {
			throw new IllegalArgumentException("Impossible de déterminer le département de la localité.");
		}
		if (!drenaDepartementRepository.existsByIdDrena_IdAndIdDepartement_Id(drenaId, deptId)) {
			throw new IllegalArgumentException("La localité n’est pas couverte par la DRENA de l’IEP sélectionné.");
		}
	}

	private void assertLocaliteLinkedToDrena(int localiteId, Integer drenaId) {
		LocaliteDImplantation loc = localiteRepository.findById(localiteId)
				.orElseThrow(() -> new IllegalArgumentException("Localité introuvable: " + localiteId));
		assertLocaliteLinkedToDrena(loc, drenaId);
	}

	private static Integer departementIdOfLocalite(LocaliteDImplantation loc) {
		SousPrefecture sp = loc.getIdSousPrefecture();
		if (sp == null) {
			return null;
		}
		Departement d = sp.getIdDepartement();
		return d != null ? d.getId() : null;
	}

	private static Integer regionIdOfLocalite(LocaliteDImplantation loc) {
		SousPrefecture sp = loc.getIdSousPrefecture();
		if (sp == null) {
			return null;
		}
		Departement d = sp.getIdDepartement();
		if (d == null || d.getIdRegion() == null) {
			return null;
		}
		return d.getIdRegion().getId();
	}

	private static boolean isNationalUser(AuthUser user) {
		return user.hasAnyRole(NATIONAL_ROLES);
	}
}
