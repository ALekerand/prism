package com.dcspa.prism.repository.spec;

import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Document;
import com.dcspa.prism.security.AuthUser;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

/**
 * Périmètre liste centres : circonscription géographique +, pour le conseiller,
 * uniquement les centres qu'il a enregistrés ({@code centre.enregistrePar}).
 * <p>
 * Coordonnateur / IEPP / superviseur : voient le périmètre géo (IEP / DRENA), incluant les saisies des subalternes.
 */
public final class CentreListScopeSpecifications {

	private CentreListScopeSpecifications() {
	}

	public static <T> Specification<T> combine(Specification<T> geographicScope, AuthUser user) {
		Specification<T> own = forConseillerOwnCentres(user);
		if (geographicScope == null && own == null) {
			return null;
		}
		if (geographicScope == null) {
			return own;
		}
		if (own == null) {
			return geographicScope;
		}
		return Specification.where(geographicScope).and(own);
	}

	public static <T> Specification<T> forConseillerOwnCentres(AuthUser user) {
		if (user == null || !user.hasRole("CONSEILLER")) {
			return null;
		}
		String me = user.getUsername();
		if (me == null || me.isBlank()) {
			return (root, query, cb) -> cb.disjunction();
		}
		return (root, query, cb) -> cb.equal(
				root.join("centre", JoinType.INNER).get("enregistrePar"),
				me.trim());
	}

	public static Specification<Centre> forCentreOwnedBy(AuthUser user) {
		if (user == null || !user.hasRole("CONSEILLER")) {
			return null;
		}
		String me = user.getUsername();
		if (me == null || me.isBlank()) {
			return (root, query, cb) -> cb.disjunction();
		}
		return (root, query, cb) -> cb.equal(root.get("enregistrePar"), me.trim());
	}

	/** Documents rattachés à un centre : le conseiller ne voit que les centres qu'il a enregistrés. */
	public static Specification<Document> forConseillerOwnDocuments(AuthUser user) {
		if (user == null || !user.hasRole("CONSEILLER")) {
			return null;
		}
		String me = user.getUsername();
		if (me == null || me.isBlank()) {
			return (root, query, cb) -> cb.disjunction();
		}
		return (root, query, cb) -> cb.equal(
				root.join("idCentre", JoinType.INNER).get("enregistrePar"),
				me.trim());
	}

	public static Specification<Document> forDocumentListScope(
			Specification<Document> geographicScope, AuthUser user) {
		return CentreCirconscriptionSpecifications.andNullable(
				geographicScope, forConseillerOwnDocuments(user));
	}

	/**
	 * Stats dashboard : même filtre géo +, pour le conseiller, uniquement ses centres
	 * ({@code enregistrePar} sur l’entité {@link Centre}).
	 */
	public static Specification<Centre> andOwnCentre(
			Specification<Centre> geographicStats, AuthUser user) {
		return CentreCirconscriptionSpecifications.andNullable(geographicStats, forCentreOwnedBy(user));
	}

	/**
	 * Stats Alpha/CEC/CP/SIE (et autres entités jointes via {@code centre}) : périmètre géo
	 * + centres enregistrés par le conseiller.
	 */
	public static <T> Specification<T> andOwnTypedCentres(Specification<T> geographicStats, AuthUser user) {
		return CentreCirconscriptionSpecifications.andNullable(geographicStats, forConseillerOwnCentres(user));
	}

	/**
	 * Personnel / documents / partenariats : {@code idCentre} est un {@link Centre}.
	 */
	public static <T> Specification<T> forConseillerOwnViaIdCentre(AuthUser user) {
		if (user == null || !user.hasRole("CONSEILLER")) {
			return null;
		}
		String me = user.getUsername();
		if (me == null || me.isBlank()) {
			return (root, query, cb) -> cb.disjunction();
		}
		return (root, query, cb) -> cb.equal(
				root.join("idCentre", JoinType.INNER).get("enregistrePar"),
				me.trim());
	}

	/**
	 * Visites / contrôles / évaluations / performances : {@code idAlpha.centre.enregistrePar}.
	 */
	public static <T> Specification<T> forConseillerOwnViaAlpha(AuthUser user) {
		if (user == null || !user.hasRole("CONSEILLER")) {
			return null;
		}
		String me = user.getUsername();
		if (me == null || me.isBlank()) {
			return (root, query, cb) -> cb.disjunction();
		}
		return (root, query, cb) -> cb.equal(
				root.join("idAlpha", JoinType.INNER).join("centre", JoinType.INNER).get("enregistrePar"),
				me.trim());
	}

	/**
	 * Effectifs : {@code idCentre} est Alpha/CEC/CP/SIE, puis {@code centre.enregistrePar}.
	 */
	public static <T> Specification<T> forConseillerOwnViaTypedCentre(AuthUser user) {
		if (user == null || !user.hasRole("CONSEILLER")) {
			return null;
		}
		String me = user.getUsername();
		if (me == null || me.isBlank()) {
			return (root, query, cb) -> cb.disjunction();
		}
		return (root, query, cb) -> cb.equal(
				root.join("idCentre", JoinType.INNER).join("centre", JoinType.INNER).get("enregistrePar"),
				me.trim());
	}

	public static <T> Specification<T> andOwnViaIdCentre(Specification<T> geographicStats, AuthUser user) {
		return CentreCirconscriptionSpecifications.andNullable(geographicStats, forConseillerOwnViaIdCentre(user));
	}

	public static <T> Specification<T> andOwnViaAlpha(Specification<T> geographicStats, AuthUser user) {
		return CentreCirconscriptionSpecifications.andNullable(geographicStats, forConseillerOwnViaAlpha(user));
	}

	public static <T> Specification<T> andOwnViaTypedCentre(Specification<T> geographicStats, AuthUser user) {
		return CentreCirconscriptionSpecifications.andNullable(geographicStats, forConseillerOwnViaTypedCentre(user));
	}
}
