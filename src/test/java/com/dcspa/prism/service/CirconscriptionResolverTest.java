package com.dcspa.prism.service;

import static org.mockito.Mockito.when;

import com.dcspa.prism.entity.Drena;
import com.dcspa.prism.entity.Iep;
import com.dcspa.prism.repository.IeppRepository;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.circonscription.CirconscriptionLevel;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CirconscriptionResolverTest {

	@Mock
	private IeppRepository iepRepository;

	@InjectMocks
	private CirconscriptionResolver resolver;

	@Test
	void superviseurSansDrenaDeriveDepuisIep() {
		Drena d = new Drena();
		d.setId(7);
		Iep iep = new Iep();
		iep.setId(3);
		iep.setIdDrena(d);
		when(iepRepository.findById(3)).thenReturn(Optional.of(iep));

		AuthUser u = new AuthUser(
				1,
				"s",
				"x",
				true,
				List.of(),
				List.of("SUPERVISEUR"),
				null,
				null,
				3,
				null,
				null,
				null,
				null);

		var att = resolver.resolve(u);
		Assertions.assertEquals(CirconscriptionLevel.DRENA, att.level());
		Assertions.assertEquals(7, att.scopeId());
	}

	@Test
	void coordinateurPrioritaireSurSuperviseurSiLesDeuxRoles() {
		AuthUser u = new AuthUser(
				1,
				"mix",
				"x",
				true,
				List.of(),
				List.of("COORDONNATEUR", "SUPERVISEUR"),
				null,
				99,
				10,
				null,
				null,
				null,
				null);

		var att = resolver.resolve(u);
		Assertions.assertEquals(CirconscriptionLevel.IEP, att.level());
		Assertions.assertEquals(10, att.scopeId());
	}

	@Test
	void nationalSansFiltre() {
		AuthUser u = new AuthUser(
				1,
				"n",
				"x",
				true,
				List.of(),
				List.of("SUPERVISEUR_AENF"),
				null,
				null,
				10,
				null,
				null,
				null,
				null);

		var att = resolver.resolve(u);
		Assertions.assertEquals(CirconscriptionLevel.NONE, att.level());
	}
}
