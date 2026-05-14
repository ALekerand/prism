package com.dcspa.prism.repository.spec;

import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.Cec;
import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.entity.Sie;
import com.dcspa.prism.service.circonscription.CirconscriptionAttachement;
import com.dcspa.prism.service.circonscription.CirconscriptionLevel;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filtre JPA commun pour les entités « fiche centre » (Alpha, CEC, CP, SIE) via leur {@code centre}.
 * <p>
 * Toujours utiliser l’{@code IEP} du centre comme source de vérité pour le périmètre IEP / DRENA.
 */
public final class CentreCirconscriptionSpecifications {

	private CentreCirconscriptionSpecifications() {
	}

	public static Specification<Alpha> forAlpha(CirconscriptionAttachement att) {
		return forCentreBacked(Alpha.class, att);
	}

	public static Specification<Cec> forCec(CirconscriptionAttachement att) {
		return forCentreBacked(Cec.class, att);
	}

	public static Specification<Cp> forCp(CirconscriptionAttachement att) {
		return forCentreBacked(Cp.class, att);
	}

	public static Specification<Sie> forSie(CirconscriptionAttachement att) {
		return forCentreBacked(Sie.class, att);
	}

	private static <T> Specification<T> forCentreBacked(Class<T> entityClass, CirconscriptionAttachement att) {
		if (att == null || att.level() == CirconscriptionLevel.NONE || att.scopeId() == null) {
			return null;
		}
		return switch (att.level()) {
			case NONE -> null;
			case IEP -> (root, query, cb) -> cb.equal(
					root.join("centre", JoinType.INNER).join("idIep", JoinType.INNER).get("id"),
					att.scopeId());
			case DRENA -> (root, query, cb) -> {
				distinctIfRootQuery(query, entityClass);
				var iepJoin = root.join("centre", JoinType.INNER).join("idIep", JoinType.INNER);
				return cb.equal(iepJoin.join("idDrena", JoinType.INNER).get("id"), att.scopeId());
			};
			case REGION -> (root, query, cb) -> {
				distinctIfRootQuery(query, entityClass);
				var centreJoin = root.join("centre", JoinType.INNER);
				var locJoin = centreJoin.join("idLocalite", JoinType.INNER);
				var spJoin = locJoin.join("idSousPrefecture", JoinType.INNER);
				var depJoin = spJoin.join("idDepartement", JoinType.INNER);
				return cb.equal(depJoin.join("idRegion", JoinType.INNER).get("id"), att.scopeId());
			};
		};
	}

	private static void distinctIfRootQuery(jakarta.persistence.criteria.CriteriaQuery<?> query, Class<?> entityClass) {
		if (query != null && entityClass.equals(query.getResultType())) {
			query.distinct(true);
		}
	}
}
