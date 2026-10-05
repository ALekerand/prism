package com.dcspa.prism.service;

import com.dcspa.prism.dto.CentreAuteurScope;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.repository.CentreRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CentreConseillerLabelServiceTest {

	@Mock
	private AppUserRepository appUserRepository;

	@Mock
	private CentreRepository centreRepository;

	@InjectMocks
	private CentreConseillerLabelService service;

	@Test
	void enrichMapsFillsConseillerAndCoordonnateurDisplayNames() {
		when(centreRepository.findAuteurScopesByIdIn(List.of(10)))
				.thenReturn(List.of(new CentreAuteurScope(10, "cons1", 3)));

		AppUser conseiller = new AppUser();
		conseiller.setUsername("cons1");
		conseiller.setPrenoms("Awa");
		conseiller.setNom("Kone");
		when(appUserRepository.findByUsernameIn(List.of("cons1"))).thenReturn(List.of(conseiller));
		when(appUserRepository.findIdentityByRoleAndIepIdIn(eq("COORDONNATEUR"), eq(List.of(3))))
				.thenReturn(List.<Object[]>of(new Object[] { "Jean", "Yao", "coord1", 3 }));

		Map<String, Object> row = new HashMap<>();
		row.put("idCentre", 10);
		service.enrichMaps(List.of(row));

		assertThat(row.get("enregistrePar")).isEqualTo("cons1");
		assertThat(row.get("conseillerNom")).isEqualTo("Awa Kone");
		assertThat(row.get("idIep")).isEqualTo(3);
		assertThat(row.get("coordonnateurNom")).isEqualTo("Jean Yao");
	}

	@Test
	void enrichWorkflowValidatorLabelsMapsLoginsToDisplayNames() {
		AppUser coord = new AppUser();
		coord.setUsername("coord1");
		coord.setPrenoms("Jean");
		coord.setNom("Yao");
		when(appUserRepository.findByUsername("coord1")).thenReturn(java.util.Optional.of(coord));

		Map<String, Object> row = new HashMap<>();
		row.put("workflowValideCoordPar", "coord1");
		service.enrichWorkflowValidatorLabels(row);

		assertThat(row.get("valideCoordNom")).isEqualTo("Jean Yao");
	}
}
