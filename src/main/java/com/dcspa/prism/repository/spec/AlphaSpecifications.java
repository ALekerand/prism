package com.dcspa.prism.repository.spec;

import com.dcspa.prism.dto.AlphaListFilter;
import com.dcspa.prism.entity.Alpha;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

// Filtres dynamiques sur les colonnes Alpha.
public final class AlphaSpecifications {

	private AlphaSpecifications() {
	}

	public static Specification<Alpha> fromFilter(AlphaListFilter f) {
		return (root, query, cb) -> {
			if (f == null) {
				return cb.conjunction();
			}
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(SpecificationSupport.eq(cb, root.get("id"), f.getId()));
			predicates.add(SpecificationSupport.eq(cb, root.get("idLocalite"), f.getIdLocalite()));
			predicates.add(SpecificationSupport.eq(cb, root.get("idPeriodicite"), f.getIdPeriodicite()));
			predicates.add(SpecificationSupport.eq(cb, root.get("idIep"), f.getIdIep()));
			if (f.getIdIep() == null) {
				predicates.add(SpecificationSupport.idIepInDrena(cb, query, root.get("idIep"), f.getIdDrena()));
			}
			predicates.add(SpecificationSupport.eq(cb, root.get("idAutoriteAutorisation"), f.getIdAutoriteAutorisation()));
			predicates.add(SpecificationSupport.eq(cb, root.get("idNaturecentre"), f.getIdNaturecentre()));
			predicates.add(SpecificationSupport.eq(cb, root.get("idPromoteur"), f.getIdPromoteur()));
			predicates.add(SpecificationSupport.eq(cb, root.get("autorisation"), f.getAutorisation()));
			predicates.add(SpecificationSupport.eq(cb, root.get("encadrerParMena"), f.getEncadrerParMena()));
			predicates.add(SpecificationSupport.eq(cb, root.get("estElectrifie"), f.getEstElectrifie()));
			predicates.add(SpecificationSupport.eq(cb, root.get("aDeLeau"), f.getADeLeau()));
			predicates.add(SpecificationSupport.eq(cb, root.get("nombreVisite"), f.getNombreVisite()));
			predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("codeCentre"), f.getCodeCentre()));
			predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("codeAlpha"), f.getCodeAlpha()));
			predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("libelleAlpha"), f.getLibelleAlpha()));
			predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("encadreurNonMena"), f.getEncadreurNonMena()));
			predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("localisationCentre"), f.getLocalisationCentre()));
			predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("nomMilieuImplentation"), f.getNomMilieuImplentation()));

			if (f.getIdCompagne() != null) {
				predicates.add(cb.equal(root.join("idCompagne", JoinType.INNER).get("id"), f.getIdCompagne()));
			}
			if (f.getIdCategorieCentreAlpha() != null) {
				predicates.add(cb.equal(root.join("idCategorieCentreAlpha", JoinType.INNER).get("id"), f.getIdCategorieCentreAlpha()));
			}
			if (f.getIdTypeAlpha() != null) {
				predicates.add(cb.equal(root.join("idTypeAlpha", JoinType.INNER).get("id"), f.getIdTypeAlpha()));
			}
			if (f.getIdRegimeAlpha() != null) {
				predicates.add(cb.equal(root.join("idRegimeAlpha", JoinType.INNER).get("id"), f.getIdRegimeAlpha()));
			}

			predicates.add(SpecificationSupport.globalTextOrId(cb, root, f.getQ(),
					"codeCentre", "codeAlpha", "libelleAlpha", "encadreurNonMena", "localisationCentre", "nomMilieuImplentation"));

			return cb.and(predicates.toArray(Predicate[]::new));
		};
	}
}
