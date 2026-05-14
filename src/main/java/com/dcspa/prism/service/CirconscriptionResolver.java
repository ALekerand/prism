package com.dcspa.prism.service;

import com.dcspa.prism.repository.IeppRepository;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.circonscription.CirconscriptionAttachement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Détermine le périmètre géographique effectif à partir du rôle et des rattachements utilisateur.
 * <p>
 * Règle : les rôles « nationaux » priment ; sinon le coordonnateur / conseiller / IEPP est borné à
 * l’IEP ; le superviseur territorial (hors AENF centrale) est borné à la DRENA (éventuellement
 * dérivée de l’IEP utilisateur) ; en dernier recours une région seule reste appliquée sur la
 * géographie des centres.
 */
@Component
@RequiredArgsConstructor
public class CirconscriptionResolver {

	private static final String[] NATIONAL_ROLES = {
			"ADMIN", "SUPER_ADMIN", "SUPER_ROOT", "SUPERVISEUR_AENF", "DIRECTEUR"
	};

	private final IeppRepository iepRepository;

	public CirconscriptionAttachement resolve(AuthUser user) {
		if (user == null) {
			return CirconscriptionAttachement.none();
		}
		if (user.hasAnyRole(NATIONAL_ROLES)) {
			return CirconscriptionAttachement.none();
		}
		if (user.hasRole("COORDONNATEUR") || user.hasRole("IEPP")) {
			if (user.getIdIep() != null) {
				return CirconscriptionAttachement.iep(user.getIdIep());
			}
			return CirconscriptionAttachement.none();
		}
		if (user.hasRole("CONSEILLER")) {
			if (user.getIdIep() != null) {
				return CirconscriptionAttachement.iep(user.getIdIep());
			}
			return CirconscriptionAttachement.none();
		}
		if (user.hasRole("SUPERVISEUR")) {
			Integer drenaId = user.getIdDrena();
			if (drenaId == null && user.getIdIep() != null) {
				drenaId = iepRepository.findById(user.getIdIep())
						.map(i -> i.getIdDrena() != null ? i.getIdDrena().getId() : null)
						.orElse(null);
			}
			if (drenaId != null) {
				return CirconscriptionAttachement.drena(drenaId);
			}
			if (user.getIdIep() != null) {
				return CirconscriptionAttachement.iep(user.getIdIep());
			}
			return CirconscriptionAttachement.none();
		}
		if (user.getIdIep() != null) {
			return CirconscriptionAttachement.iep(user.getIdIep());
		}
		if (user.getIdRegion() != null) {
			return CirconscriptionAttachement.region(user.getIdRegion());
		}
		return CirconscriptionAttachement.none();
	}
}
