package com.dcspa.prism.repository.spec;

import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.security.AuthUser;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

/**
 * Restreint les alphas au périmètre géographique de l’utilisateur (IEP, puis DRENA, puis région).
 * Les rôles {@code ADMIN}, {@code SUPER_ADMIN}, {@code SUPER_ROOT} ne sont pas restreints.
 */
public final class AlphaCirconscriptionScope {

	private static final String[] ADMIN_ROLES = { "ADMIN", "SUPER_ADMIN", "SUPER_ROOT" };

	private AlphaCirconscriptionScope() {
	}

	/**
	 * @return une spécification à combiner avec les filtres métier, ou {@code null} si aucune restriction.
	 */
	public static Specification<Alpha> specification(AuthUser user) {
		if (user == null || user.hasAnyRole(ADMIN_ROLES)) {
			return null;
		}
		if (user.getIdIep() != null) {
			Integer iepId = user.getIdIep();
			return (root, query, cb) -> cb.equal(root.get("idIep"), iepId);
		}
		if (user.getIdDrena() != null) {
			Integer drenaId = user.getIdDrena();
			return (root, query, cb) -> {
				if (query != null && Alpha.class.equals(query.getResultType())) {
					query.distinct(true);
				}
				var iepJoin = root.join("centre", JoinType.INNER).join("idIep", JoinType.INNER);
				return cb.equal(iepJoin.get("idDrena").get("id"), drenaId);
			};
		}
		if (user.getIdRegion() != null) {
			Integer regionId = user.getIdRegion();
			return (root, query, cb) -> {
				if (query != null && Alpha.class.equals(query.getResultType())) {
					query.distinct(true);
				}
				var centreJoin = root.join("centre", JoinType.INNER);
				var locJoin = centreJoin.join("idLocalite", JoinType.INNER);
				var spJoin = locJoin.join("idSousPrefecture", JoinType.INNER);
				var depJoin = spJoin.join("idDepartement", JoinType.INNER);
				return cb.equal(depJoin.get("idRegion").get("id"), regionId);
			};
		}
		return null;
	}
}
