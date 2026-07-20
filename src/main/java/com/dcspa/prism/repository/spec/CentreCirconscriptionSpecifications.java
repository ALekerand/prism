package com.dcspa.prism.repository.spec;

import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.AppuiPartenaire;
import com.dcspa.prism.entity.Document;
import com.dcspa.prism.entity.Cec;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Controle;
import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.entity.Evaluation;
import com.dcspa.prism.entity.Performance;
import com.dcspa.prism.entity.Personnel;
import com.dcspa.prism.entity.Sie;
import com.dcspa.prism.entity.Visite;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.entity.DrenaDepartement;
import com.dcspa.prism.service.circonscription.CirconscriptionAttachement;
import com.dcspa.prism.service.circonscription.CirconscriptionLevel;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

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

	public static Specification<Centre> forCentre(CirconscriptionAttachement att) {
		if (att == null || att.level() == CirconscriptionLevel.NONE || att.scopeId() == null) {
			return null;
		}
		return switch (att.level()) {
			case NONE -> null;
			case IEP -> (root, query, cb) -> cb.equal(
					root.join("idIep", JoinType.INNER).get("id"),
					att.scopeId());
			case DRENA -> (root, query, cb) -> {
				distinctIfRootQuery(query, Centre.class);
				var iepJoin = root.join("idIep", JoinType.INNER);
				return cb.equal(iepJoin.join("idDrena", JoinType.INNER).get("id"), att.scopeId());
			};
			case REGION -> (root, query, cb) -> {
				distinctIfRootQuery(query, Centre.class);
				var locJoin = root.join("idLocalite", JoinType.INNER);
				var spJoin = locJoin.join("idSousPrefecture", JoinType.INNER);
				var depJoin = spJoin.join("idDepartement", JoinType.INNER);
				return cb.equal(depJoin.join("idRegion", JoinType.INNER).get("id"), att.scopeId());
			};
		};
	}

	public static Specification<Personnel> forPersonnel(CirconscriptionAttachement att) {
		if (att == null || att.level() == CirconscriptionLevel.NONE || att.scopeId() == null) {
			return null;
		}
		return switch (att.level()) {
			case NONE -> null;
			case IEP -> (root, query, cb) -> cb.equal(
					root.join("idCentre", JoinType.INNER).join("idIep", JoinType.INNER).get("id"),
					att.scopeId());
			case DRENA -> (root, query, cb) -> {
				distinctIfRootQuery(query, Personnel.class);
				var iepJoin = root.join("idCentre", JoinType.INNER).join("idIep", JoinType.INNER);
				return cb.equal(iepJoin.join("idDrena", JoinType.INNER).get("id"), att.scopeId());
			};
			case REGION -> (root, query, cb) -> {
				distinctIfRootQuery(query, Personnel.class);
				var centreJoin = root.join("idCentre", JoinType.INNER);
				var locJoin = centreJoin.join("idLocalite", JoinType.INNER);
				var spJoin = locJoin.join("idSousPrefecture", JoinType.INNER);
				var depJoin = spJoin.join("idDepartement", JoinType.INNER);
				return cb.equal(depJoin.join("idRegion", JoinType.INNER).get("id"), att.scopeId());
			};
		};
	}

	public static Specification<Visite> forVisite(CirconscriptionAttachement att) {
		return forAlphaLinked(Visite.class, att, "idAlpha");
	}

	public static Specification<Controle> forControle(CirconscriptionAttachement att) {
		return forAlphaLinked(Controle.class, att, "idAlpha");
	}

	public static Specification<Evaluation> forEvaluation(CirconscriptionAttachement att) {
		return forAlphaLinked(Evaluation.class, att, "idAlpha");
	}

	public static Specification<Performance> forPerformance(CirconscriptionAttachement att) {
		return forAlphaLinked(Performance.class, att, "idAlpha");
	}

	public static Specification<Document> forDocument(CirconscriptionAttachement att) {
		if (att == null || att.level() == CirconscriptionLevel.NONE || att.scopeId() == null) {
			return null;
		}
		return switch (att.level()) {
			case NONE -> null;
			case IEP -> (root, query, cb) -> cb.equal(
					root.join("idCentre", JoinType.INNER).join("idIep", JoinType.INNER).get("id"),
					att.scopeId());
			case DRENA -> (root, query, cb) -> {
				distinctIfRootQuery(query, Document.class);
				var iepJoin = root.join("idCentre", JoinType.INNER).join("idIep", JoinType.INNER);
				return cb.equal(iepJoin.join("idDrena", JoinType.INNER).get("id"), att.scopeId());
			};
			case REGION -> (root, query, cb) -> {
				distinctIfRootQuery(query, Document.class);
				var centreJoin = root.join("idCentre", JoinType.INNER);
				var locJoin = centreJoin.join("idLocalite", JoinType.INNER);
				var spJoin = locJoin.join("idSousPrefecture", JoinType.INNER);
				var depJoin = spJoin.join("idDepartement", JoinType.INNER);
				return cb.equal(depJoin.join("idRegion", JoinType.INNER).get("id"), att.scopeId());
			};
		};
	}

	public static Specification<AppuiPartenaire> forAppuiPartenaire(CirconscriptionAttachement att) {
		if (att == null || att.level() == CirconscriptionLevel.NONE || att.scopeId() == null) {
			return null;
		}
		return switch (att.level()) {
			case NONE -> null;
			case IEP -> (root, query, cb) -> cb.equal(
					root.join("idCentre", JoinType.INNER).join("idIep", JoinType.INNER).get("id"),
					att.scopeId());
			case DRENA -> (root, query, cb) -> {
				distinctIfRootQuery(query, AppuiPartenaire.class);
				var iepJoin = root.join("idCentre", JoinType.INNER).join("idIep", JoinType.INNER);
				return cb.equal(iepJoin.join("idDrena", JoinType.INNER).get("id"), att.scopeId());
			};
			case REGION -> (root, query, cb) -> {
				distinctIfRootQuery(query, AppuiPartenaire.class);
				var centreJoin = root.join("idCentre", JoinType.INNER);
				var locJoin = centreJoin.join("idLocalite", JoinType.INNER);
				var spJoin = locJoin.join("idSousPrefecture", JoinType.INNER);
				var depJoin = spJoin.join("idDepartement", JoinType.INNER);
				return cb.equal(depJoin.join("idRegion", JoinType.INNER).get("id"), att.scopeId());
			};
		};
	}

	/**
	 * Utilisateurs visibles sous la « coupole » du profil connecté.
	 * <p>
	 * DRENA : rattachement direct à la DRENA ou à une IEPP de cette DRENA.
	 * Région : rattachement direct ou chaîne département → région (y compris via DRENA / IEPP).
	 */
	public static Specification<AppUser> forAppUser(CirconscriptionAttachement att) {
		if (att == null || att.level() == CirconscriptionLevel.NONE || att.scopeId() == null) {
			return null;
		}
		return switch (att.level()) {
			case NONE -> null;
			case IEP -> (root, query, cb) -> cb.equal(
					root.join("idIep", JoinType.INNER).get("id"),
					att.scopeId());
			case DRENA -> (root, query, cb) -> {
				distinctIfRootQuery(query, AppUser.class);
				var drenaDirect = cb.equal(
						root.join("idDrena", JoinType.INNER).get("id"),
						att.scopeId());
				var iepUnderDrena = cb.equal(
						root.join("idIep", JoinType.INNER).join("idDrena", JoinType.INNER).get("id"),
						att.scopeId());
				return cb.or(drenaDirect, iepUnderDrena);
			};
			case REGION -> (root, query, cb) -> {
				distinctIfRootQuery(query, AppUser.class);
				int regionId = att.scopeId();
				List<Predicate> ors = new ArrayList<>();
				ors.add(cb.equal(root.join("idRegion", JoinType.INNER).get("id"), regionId));
				ors.add(cb.equal(
						root.join("idDepartement", JoinType.INNER).join("idRegion", JoinType.INNER).get("id"),
						regionId));
				ors.add(cb.equal(
						root.join("idSousPrefecture", JoinType.INNER)
								.join("idDepartement", JoinType.INNER)
								.join("idRegion", JoinType.INNER)
								.get("id"),
						regionId));
				ors.add(cb.equal(
						root.join("idLocalite", JoinType.INNER)
								.join("idSousPrefecture", JoinType.INNER)
								.join("idDepartement", JoinType.INNER)
								.join("idRegion", JoinType.INNER)
								.get("id"),
						regionId));
				ors.add(userDrenaInRegion(root, query, cb, regionId));
				ors.add(userIepDrenaInRegion(root, query, cb, regionId));
				return cb.or(ors.toArray(Predicate[]::new));
			};
		};
	}

	private static Predicate userDrenaInRegion(
			jakarta.persistence.criteria.Root<AppUser> root,
			jakarta.persistence.criteria.CriteriaQuery<?> query,
			jakarta.persistence.criteria.CriteriaBuilder cb,
			int regionId) {
		Subquery<Integer> sq = query.subquery(Integer.class);
		var ddRoot = sq.from(DrenaDepartement.class);
		sq.select(cb.literal(1)).where(
				cb.and(
						cb.equal(ddRoot.get("idDrena").get("id"), root.get("idDrena").get("id")),
						cb.equal(
								ddRoot.join("idDepartement", JoinType.INNER)
										.join("idRegion", JoinType.INNER)
										.get("id"),
								regionId)));
		return cb.and(cb.isNotNull(root.get("idDrena")), cb.exists(sq));
	}

	private static Predicate userIepDrenaInRegion(
			jakarta.persistence.criteria.Root<AppUser> root,
			jakarta.persistence.criteria.CriteriaQuery<?> query,
			jakarta.persistence.criteria.CriteriaBuilder cb,
			int regionId) {
		Subquery<Integer> sq = query.subquery(Integer.class);
		var ddRoot = sq.from(DrenaDepartement.class);
		var iepDrena = root.join("idIep", JoinType.INNER).join("idDrena", JoinType.INNER);
		sq.select(cb.literal(1)).where(
				cb.and(
						cb.equal(ddRoot.get("idDrena").get("id"), iepDrena.get("id")),
						cb.equal(
								ddRoot.join("idDepartement", JoinType.INNER)
										.join("idRegion", JoinType.INNER)
										.get("id"),
								regionId)));
		return cb.and(cb.isNotNull(root.get("idIep")), cb.exists(sq));
	}

	private static <T> Specification<T> forAlphaLinked(
			Class<T> entityClass,
			CirconscriptionAttachement att,
			String alphaField) {
		if (att == null || att.level() == CirconscriptionLevel.NONE || att.scopeId() == null) {
			return null;
		}
		return switch (att.level()) {
			case NONE -> null;
			case IEP -> (root, query, cb) -> cb.equal(
					root.join(alphaField, JoinType.INNER).join("centre", JoinType.INNER).join("idIep", JoinType.INNER).get("id"),
					att.scopeId());
			case DRENA -> (root, query, cb) -> {
				distinctIfRootQuery(query, entityClass);
				var iepJoin = root.join(alphaField, JoinType.INNER).join("centre", JoinType.INNER).join("idIep", JoinType.INNER);
				return cb.equal(iepJoin.join("idDrena", JoinType.INNER).get("id"), att.scopeId());
			};
			case REGION -> (root, query, cb) -> {
				distinctIfRootQuery(query, entityClass);
				var centreJoin = root.join(alphaField, JoinType.INNER).join("centre", JoinType.INNER);
				var locJoin = centreJoin.join("idLocalite", JoinType.INNER);
				var spJoin = locJoin.join("idSousPrefecture", JoinType.INNER);
				var depJoin = spJoin.join("idDepartement", JoinType.INNER);
				return cb.equal(depJoin.join("idRegion", JoinType.INNER).get("id"), att.scopeId());
			};
		};
	}

	public static <T> Specification<T> forCentreBacked(Class<T> entityClass, CirconscriptionAttachement att) {
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

	/** {@code null} ou {@code true} = actif (inclus dans les statistiques). */
	public static boolean isActif(Centre centre) {
		if (centre == null) {
			return false;
		}
		Boolean actif = centre.getActif();
		return actif == null || Boolean.TRUE.equals(actif);
	}

	public static Specification<Centre> centreActifOnly() {
		return (root, query, cb) -> cb.or(cb.isNull(root.get("actif")), cb.isTrue(root.get("actif")));
	}

	public static <T> Specification<T> centreBackedActifOnly() {
		return (root, query, cb) -> {
			var centre = root.join("centre", JoinType.INNER);
			return cb.or(cb.isNull(centre.get("actif")), cb.isTrue(centre.get("actif")));
		};
	}

	public static <T> Specification<T> idCentreActifOnly() {
		return (root, query, cb) -> {
			var centre = root.join("idCentre", JoinType.INNER);
			return cb.or(cb.isNull(centre.get("actif")), cb.isTrue(centre.get("actif")));
		};
	}

	public static <T> Specification<T> alphaLinkedCentreActifOnly(String alphaField) {
		return (root, query, cb) -> {
			var centre = root.join(alphaField, JoinType.INNER).join("centre", JoinType.INNER);
			return cb.or(cb.isNull(centre.get("actif")), cb.isTrue(centre.get("actif")));
		};
	}

	public static <T> Specification<T> andNullable(Specification<T> first, Specification<T> second) {
		if (first == null && second == null) {
			return null;
		}
		if (first == null) {
			return second;
		}
		if (second == null) {
			return first;
		}
		return first.and(second);
	}

	public static Specification<Centre> forCentreStats(CirconscriptionAttachement att) {
		return andNullable(forCentre(att), centreActifOnly());
	}

	public static Specification<Alpha> forAlphaStats(CirconscriptionAttachement att) {
		return andNullable(forAlpha(att), centreBackedActifOnly());
	}

	public static Specification<Cec> forCecStats(CirconscriptionAttachement att) {
		return andNullable(forCec(att), centreBackedActifOnly());
	}

	public static Specification<Cp> forCpStats(CirconscriptionAttachement att) {
		return andNullable(forCp(att), centreBackedActifOnly());
	}

	public static Specification<Sie> forSieStats(CirconscriptionAttachement att) {
		return andNullable(forSie(att), centreBackedActifOnly());
	}

	public static Specification<Personnel> forPersonnelStats(CirconscriptionAttachement att) {
		return andNullable(forPersonnel(att), idCentreActifOnly());
	}

	public static Specification<Visite> forVisiteStats(CirconscriptionAttachement att) {
		return andNullable(forVisite(att), alphaLinkedCentreActifOnly("idAlpha"));
	}

	public static Specification<Controle> forControleStats(CirconscriptionAttachement att) {
		return andNullable(forControle(att), alphaLinkedCentreActifOnly("idAlpha"));
	}

	public static Specification<Evaluation> forEvaluationStats(CirconscriptionAttachement att) {
		return andNullable(forEvaluation(att), alphaLinkedCentreActifOnly("idAlpha"));
	}

	public static Specification<Performance> forPerformanceStats(CirconscriptionAttachement att) {
		return andNullable(forPerformance(att), alphaLinkedCentreActifOnly("idAlpha"));
	}

	public static Specification<Document> forDocumentStats(CirconscriptionAttachement att) {
		return andNullable(forDocument(att), idCentreActifOnly());
	}

	public static Specification<AppuiPartenaire> forAppuiPartenaireStats(CirconscriptionAttachement att) {
		return andNullable(forAppuiPartenaire(att), idCentreActifOnly());
	}

	public static <T> Specification<T> forCentreBackedStats(Class<T> entityClass, CirconscriptionAttachement att) {
		return andNullable(forCentreBacked(entityClass, att), centreBackedActifOnly());
	}
}
