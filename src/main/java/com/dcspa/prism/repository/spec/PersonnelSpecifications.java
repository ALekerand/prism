package com.dcspa.prism.repository.spec;

import com.dcspa.prism.dto.PersonnelListFilter;
import com.dcspa.prism.entity.Personnel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class PersonnelSpecifications {

	private PersonnelSpecifications() {
	}

	public static Specification<Personnel> byCentreAndFilter(Integer centreId, PersonnelListFilter f) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.equal(root.get("idCentre").get("id"), centreId));

			if (f != null) {
				if (f.getIdFonction() != null) {
					predicates.add(cb.equal(root.get("idFonction").get("id"), f.getIdFonction()));
				}
				if (f.getIdStatutPersonnel() != null) {
					predicates.add(cb.equal(root.get("idStatutPersonnel").get("id"), f.getIdStatutPersonnel()));
				}
				if (f.getIdNiveauPersonnel() != null) {
					predicates.add(cb.equal(root.get("idNiveauPersonnel").get("id"), f.getIdNiveauPersonnel()));
				}
				if (f.getIdCivilite() != null) {
					predicates.add(cb.equal(root.get("idCivilite").get("id"), f.getIdCivilite()));
				}
				if (f.getSexePersonnel() != null && !f.getSexePersonnel().isBlank()) {
					predicates.add(cb.equal(root.get("sexePersonnel"), f.getSexePersonnel().trim()));
				}
				predicates.add(SpecificationSupport.globalTextOrId(cb, root, f.getQ(),
						"nomPersonnel", "prenomsPersonnel", "contactPersonnel", "emailPersonnel", "codePersonnel"));
			}

			return cb.and(predicates.toArray(Predicate[]::new));
		};
	}
}
