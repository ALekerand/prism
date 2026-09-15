package com.dcspa.prism.repository.spec;

import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.security.AuthUser;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

class CentreListScopeSpecificationsTest {

	@Test
	void conseillerScopeRestrictsToOwnCentres() {
		AuthUser conseiller = new AuthUser(
				1,
				"conseiller1",
				"x",
				true,
				List.of(),
				List.of("CONSEILLER"),
				null,
				null,
				10,
				null,
				null,
				null,
				null);

		Specification<Alpha> combined = CentreListScopeSpecifications.combine(null, conseiller);
		Assertions.assertNotNull(combined);
	}

	@Test
	void coordinateurHasNoOwnershipFilter() {
		AuthUser coord = new AuthUser(
				2,
				"coord1",
				"x",
				true,
				List.of(),
				List.of("COORDONNATEUR"),
				null,
				null,
				10,
				null,
				null,
				null,
				null);

		Assertions.assertNull(CentreListScopeSpecifications.forConseillerOwnCentres(coord));
		Assertions.assertNull(CentreListScopeSpecifications.forConseillerOwnViaIdCentre(coord));
		Assertions.assertNull(CentreListScopeSpecifications.forConseillerOwnViaAlpha(coord));
	}

	@Test
	void conseillerGetsOwnershipFiltersOnPersonnelAndVisites() {
		AuthUser conseiller = new AuthUser(
				1,
				"conseiller1",
				"x",
				true,
				List.of(),
				List.of("CONSEILLER"),
				null,
				null,
				10,
				null,
				null,
				null,
				null);
		Assertions.assertNotNull(CentreListScopeSpecifications.forConseillerOwnViaIdCentre(conseiller));
		Assertions.assertNotNull(CentreListScopeSpecifications.forConseillerOwnViaAlpha(conseiller));
		Assertions.assertNotNull(CentreListScopeSpecifications.forConseillerOwnViaTypedCentre(conseiller));
	}
}
