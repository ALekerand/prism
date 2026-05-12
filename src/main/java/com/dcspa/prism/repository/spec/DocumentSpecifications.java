package com.dcspa.prism.repository.spec;

import com.dcspa.prism.dto.DocumentListFilter;
import com.dcspa.prism.entity.Document;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class DocumentSpecifications {

	private DocumentSpecifications() {
	}

	public static Specification<Document> fromFilter(DocumentListFilter f) {
		return (root, query, cb) -> {
			if (f == null) {
				return cb.conjunction();
			}
			if (query != null && Document.class.equals(query.getResultType())) {
				query.distinct(true);
			}
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(SpecificationSupport.eq(cb, root.get("id"), f.getId()));
			predicates.add(SpecificationSupport.containsIgnoreCase(cb, root.get("codeDocument"), f.getCodeDocument()));

			Join<?, ?> centreJoin = null;
			Join<?, ?> natureJoin = null;
			Join<?, ?> typeJoin = null;

			if (f.getIdCentre() != null) {
				centreJoin = root.join("idCentre", JoinType.INNER);
				predicates.add(cb.equal(centreJoin.get("id"), f.getIdCentre()));
			}
			if (f.getIdNatureDocument() != null) {
				natureJoin = root.join("idNatureDocument", JoinType.INNER);
				predicates.add(cb.equal(natureJoin.get("id"), f.getIdNatureDocument()));
			}
			if (f.getIdTypeDocument() != null) {
				typeJoin = root.join("idTypeDocument", JoinType.INNER);
				predicates.add(cb.equal(typeJoin.get("id"), f.getIdTypeDocument()));
			}

			String q = f.getQ();
			if (q != null && !q.isBlank()) {
				String t = q.trim();
				if (centreJoin == null) {
					centreJoin = root.join("idCentre", JoinType.INNER);
				}
				if (natureJoin == null) {
					natureJoin = root.join("idNatureDocument", JoinType.INNER);
				}
				if (typeJoin == null) {
					typeJoin = root.join("idTypeDocument", JoinType.INNER);
				}
				List<Predicate> ors = new ArrayList<>();
				ors.add(SpecificationSupport.containsIgnoreCase(cb, root.get("codeDocument"), t));
				ors.add(SpecificationSupport.containsIgnoreCase(cb, centreJoin.get("codeCentre"), t));
				ors.add(SpecificationSupport.containsIgnoreCase(cb, centreJoin.get("localisationCentre"), t));
				ors.add(SpecificationSupport.containsIgnoreCase(cb, natureJoin.get("libelleNatureDocument"), t));
				ors.add(SpecificationSupport.containsIgnoreCase(cb, typeJoin.get("codeTypeDocument"), t));
				ors.add(SpecificationSupport.containsIgnoreCase(cb, typeJoin.get("libelleTypeDocument"), t));
				try {
					ors.add(cb.equal(root.get("id"), Integer.valueOf(t)));
				} catch (NumberFormatException ignored) {
					// q n’est pas un id entier
				}
				predicates.add(cb.or(ors.toArray(Predicate[]::new)));
			}

			return cb.and(predicates.toArray(Predicate[]::new));
		};
	}
}
