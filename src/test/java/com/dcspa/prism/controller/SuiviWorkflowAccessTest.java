package com.dcspa.prism.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.dcspa.prism.entity.SuiviSuperviseur;
import com.dcspa.prism.entity.SuiviIepp;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.SuiviIeppRepository;
import com.dcspa.prism.repository.SuiviSuperviseurRepository;
import com.dcspa.prism.security.AuthUser;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class SuiviWorkflowAccessTest {

	@Test
	void conseillerCannotReadIeppFollowupRows() {
		SuiviIeppRepository repository = mock(SuiviIeppRepository.class);
		SuiviIeppController controller = new SuiviIeppController(repository, mock(AlphaRepository.class));

		ResponseEntity<?> response = controller.findAll(user("POINTS_VISITES:LIRE", "VALIDATION_VISITES_CONSEILLER:VALIDER"));

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		verifyNoInteractions(repository);
	}

	@Test
	void centraleOnlyReadsValidatedIeppRows() {
		SuiviIeppRepository repository = mock(SuiviIeppRepository.class);
		SuiviIepp draft = new SuiviIepp();
		draft.setId(1);
		draft.setValideeIepp(false);
		SuiviIepp validated = new SuiviIepp();
		validated.setId(2);
		validated.setValideeIepp(true);
		when(repository.findAll()).thenReturn(List.of(draft, validated));
		SuiviIeppController controller = new SuiviIeppController(repository, mock(AlphaRepository.class));

		ResponseEntity<?> response = controller.findAll(user("SUIVI_CENTRALE:LIRE"));

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).asList()
				.singleElement()
				.extracting(row -> ((Map<?, ?>) row).get("id"))
				.isEqualTo(2);
	}

	@Test
	void centraleOnlyReadsValidatedSupervisorRows() {
		SuiviSuperviseurRepository repository = mock(SuiviSuperviseurRepository.class);
		SuiviSuperviseur draft = new SuiviSuperviseur();
		draft.setId(1);
		draft.setValideeSuperviseur(false);
		SuiviSuperviseur validated = new SuiviSuperviseur();
		validated.setId(2);
		validated.setValideeSuperviseur(true);
		when(repository.findAll()).thenReturn(List.of(draft, validated));
		SuiviSuperviseurController controller = new SuiviSuperviseurController(repository, mock(AlphaRepository.class));

		ResponseEntity<?> response = controller.findAll(user("SUIVI_CENTRALE:LIRE"));

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).asList()
				.singleElement()
				.extracting(row -> ((Map<?, ?>) row).get("id"))
				.isEqualTo(2);
	}

	private AuthUser user(String... permissions) {
		return new AuthUser(1, "test", "hash", true, List.of(permissions));
	}
}
