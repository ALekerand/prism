package com.dcspa.prism.repository.spec;

import com.dcspa.prism.dto.CecListFilter;
import com.dcspa.prism.dto.CpListFilter;
import com.dcspa.prism.dto.SimpleCentreListFilterBase;
import com.dcspa.prism.dto.SieListFilter;
import com.dcspa.prism.entity.Cec;
import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.entity.Sie;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

// Filtres dynamiques pour CEC, CP et SIE (mêmes colonnes dénormalisées).
public final class SimpleCentreTypeSpecifications {

	private SimpleCentreTypeSpecifications() {
	}

	public static Specification<Cec> forCec(CecListFilter f) {
		return (root, query, cb) -> {
			if (f == null) {
				return cb.conjunction();
			}
			List<Predicate> predicates = basePredicates(root, cb, f);
			predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("libelleCec"), f.getLibelleCec()));
			predicates.add(globalQ(cb, root, f.getQ(), "libelleCec"));
			return cb.and(predicates.toArray(Predicate[]::new));
		};
	}

	public static Specification<Cp> forCp(CpListFilter f) {
		return (root, query, cb) -> {
			if (f == null) {
				return cb.conjunction();
			}
			List<Predicate> predicates = basePredicates(root, cb, f);
			predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("libellleCp"), f.getLibellleCp()));
			predicates.add(globalQ(cb, root, f.getQ(), "libellleCp"));
			return cb.and(predicates.toArray(Predicate[]::new));
		};
	}

	public static Specification<Sie> forSie(SieListFilter f) {
		return (root, query, cb) -> {
			if (f == null) {
				return cb.conjunction();
			}
			List<Predicate> predicates = basePredicates(root, cb, f);
			predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("libelleSie"), f.getLibelleSie()));
			predicates.add(globalQ(cb, root, f.getQ(), "libelleSie"));
			return cb.and(predicates.toArray(Predicate[]::new));
		};
	}

	private static <T> Predicate globalQ(CriteriaBuilder cb, Root<T> root, String q, String libelleAttr) {
		return SpecificationSupport.globalTextOrId(cb, root, q,
				libelleAttr, "codeCentre", "encadreurNonMena", "localisationCentre", "nomMilieuImplentation");
	}

	private static <T> List<Predicate> basePredicates(Root<T> root, CriteriaBuilder cb, SimpleCentreListFilterBase f) {
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(SpecificationSupport.eq(cb, root.get("id"), f.getId()));
		predicates.add(SpecificationSupport.eq(cb, root.get("idLocalite"), f.getIdLocalite()));
		predicates.add(SpecificationSupport.eq(cb, root.get("idPeriodicite"), f.getIdPeriodicite()));
		predicates.add(SpecificationSupport.eq(cb, root.get("idIep"), f.getIdIep()));
		predicates.add(SpecificationSupport.eq(cb, root.get("idAutoriteAutorisation"), f.getIdAutoriteAutorisation()));
		predicates.add(SpecificationSupport.eq(cb, root.get("idNaturecentre"), f.getIdNaturecentre()));
		predicates.add(SpecificationSupport.eq(cb, root.get("idPromoteur"), f.getIdPromoteur()));
		predicates.add(SpecificationSupport.eq(cb, root.get("autorisation"), f.getAutorisation()));
		predicates.add(SpecificationSupport.eq(cb, root.get("encadrerParMena"), f.getEncadrerParMena()));
		predicates.add(SpecificationSupport.eq(cb, root.get("estElectrifie"), f.getEstElectrifie()));
		predicates.add(SpecificationSupport.eq(cb, root.get("aDeLeau"), f.getADeLeau()));
		predicates.add(SpecificationSupport.eq(cb, root.get("nombreVisite"), f.getNombreVisite()));
		predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("codeCentre"), f.getCodeCentre()));
		predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("encadreurNonMena"), f.getEncadreurNonMena()));
		predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("localisationCentre"), f.getLocalisationCentre()));
		predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("nomMilieuImplentation"), f.getNomMilieuImplentation()));
		return predicates;
	}
}
