package com.dcspa.prism.repository.spec;

import com.dcspa.prism.dto.PromoteurListFilter;
import com.dcspa.prism.entity.Promoteur;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class PromoteurSpecifications {

	private PromoteurSpecifications() {
	}

	public static Specification<Promoteur> byFilter(PromoteurListFilter f) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (f == null) {
				return cb.conjunction();
			}
			if (f.getTypePromoteur() != null && !f.getTypePromoteur().isBlank()) {
				predicates.add(cb.equal(cb.upper(root.get("typePromoteur")), f.getTypePromoteur().trim().toUpperCase()));
			}
			if (f.getCodePromoteur() != null && !f.getCodePromoteur().isBlank()) {
				predicates.add(cb.like(cb.lower(root.get("codePromoteur")), "%" + f.getCodePromoteur().trim().toLowerCase() + "%"));
			}
			if (f.getLibellePromoteur() != null && !f.getLibellePromoteur().isBlank()) {
				predicates.add(cb.like(cb.lower(root.get("libellePromoteur")), "%" + f.getLibellePromoteur().trim().toLowerCase() + "%"));
			}
			predicates.add(SpecificationSupport.globalTextOrId(cb, root, f.getQ(), "codePromoteur", "libellePromoteur"));
			return cb.and(predicates.toArray(Predicate[]::new));
		};
	}
}
