package com.dcspa.prism.repository.spec;

import com.dcspa.prism.entity.Centre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

class CentreCirconscriptionSpecificationsActifTest {

	@Test
	void isActifTreatsNullAndTrueAsActive() {
		Centre activeNull = new Centre();
		activeNull.setActif(null);
		Centre activeTrue = new Centre();
		activeTrue.setActif(Boolean.TRUE);
		Centre inactive = new Centre();
		inactive.setActif(Boolean.FALSE);

		Assertions.assertTrue(CentreCirconscriptionSpecifications.isActif(activeNull));
		Assertions.assertTrue(CentreCirconscriptionSpecifications.isActif(activeTrue));
		Assertions.assertFalse(CentreCirconscriptionSpecifications.isActif(inactive));
		Assertions.assertFalse(CentreCirconscriptionSpecifications.isActif(null));
	}

	@Test
	void andNullableCombinesSpecifications() {
		Specification<Object> alwaysTrue = (root, query, cb) -> cb.conjunction();
		Specification<Object> alwaysFalse = (root, query, cb) -> cb.disjunction();

		Specification<Object> both = CentreCirconscriptionSpecifications.andNullable(alwaysTrue, alwaysFalse);
		Assertions.assertNotNull(both);

		Specification<Object> leftOnly = CentreCirconscriptionSpecifications.andNullable(alwaysTrue, null);
		Assertions.assertSame(alwaysTrue, leftOnly);

		Specification<Object> rightOnly = CentreCirconscriptionSpecifications.andNullable(null, alwaysFalse);
		Assertions.assertSame(alwaysFalse, rightOnly);

		Assertions.assertNull(CentreCirconscriptionSpecifications.andNullable(null, null));
	}
}
