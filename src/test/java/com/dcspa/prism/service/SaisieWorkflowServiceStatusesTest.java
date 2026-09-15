package com.dcspa.prism.service;

import com.dcspa.prism.entity.SaisieWorkflow;
import com.dcspa.prism.entity.SaisieWorkflowStatus;
import com.dcspa.prism.repository.SaisieWorkflowRepository;
import com.dcspa.prism.security.AuthUser;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaisieWorkflowServiceStatusesTest {

	@Mock
	private SaisieWorkflowRepository repository;

	@InjectMocks
	private SaisieWorkflowService service;

	@Test
	void statusesReturnsDraftWhenNoWorkflowRow() {
		AuthUser conseiller = new AuthUser(
				1,
				"commission_1_conseiller_2",
				"x",
				true,
				List.of("SAISIE_DONNEES:MODIFIER"),
				List.of("CONSEILLER"),
				null,
				null,
				1,
				null,
				null,
				null,
				null);
		when(repository.findByResourcePathAndRecordIdIn(eq("/api/effectif-alpha"), anyCollection()))
				.thenReturn(List.of());

		Map<Integer, Map<String, Object>> statuses =
				service.statuses("/api/effectif-alpha", List.of(1), conseiller);

		assertThat(statuses).containsKey(1);
		assertThat(statuses.get(1).get("workflowStatut")).isEqualTo("BROUILLON");
		assertThat(statuses.get(1).get("workflowEditable")).isEqualTo(true);
	}

	@Test
	void statusesMarksSoumisAsNotEditableForOwner() {
		AuthUser conseiller = new AuthUser(
				2,
				"commission_1_conseiller_2",
				"x",
				true,
				List.of(),
				List.of("CONSEILLER"),
				null,
				null,
				1,
				null,
				null,
				null,
				null);
		SaisieWorkflow workflow = new SaisieWorkflow();
		workflow.setResourcePath("/api/effectif-alpha");
		workflow.setRecordId(1);
		workflow.setStatut(SaisieWorkflowStatus.SOUMIS);
		workflow.setProprietaire("commission_1_conseiller_2");
		workflow.setSoumisPar("commission_1_conseiller_2");
		when(repository.findByResourcePathAndRecordIdIn(eq("/api/effectif-alpha"), anyCollection()))
				.thenReturn(List.of(workflow));

		Map<Integer, Map<String, Object>> statuses =
				service.statuses("/api/effectif-alpha", List.of(1), conseiller);

		assertThat(statuses.get(1).get("workflowStatut")).isEqualTo("SOUMIS");
		assertThat(statuses.get(1).get("workflowEditable")).isEqualTo(false);
	}

	@Test
	void submitKeepsPreviousReturnComment() {
		AuthUser conseiller = new AuthUser(
				3,
				"commission_1_conseiller_2",
				"x",
				true,
				List.of("SAISIE_DONNEES:MODIFIER"),
				List.of("CONSEILLER"),
				null,
				null,
				1,
				null,
				null,
				null,
				null);
		SaisieWorkflow workflow = new SaisieWorkflow();
		workflow.setResourcePath("/api/effectif-alpha");
		workflow.setRecordId(1);
		workflow.setStatut(SaisieWorkflowStatus.RETOURNE);
		workflow.setProprietaire("commission_1_conseiller_2");
		workflow.setCommentaireRetour("Corriger les totaux H/F");
		when(repository.findByResourcePathAndRecordId(eq("/api/effectif-alpha"), eq(1)))
				.thenReturn(Optional.of(workflow));
		when(repository.save(org.mockito.ArgumentMatchers.any(SaisieWorkflow.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		ResponseEntity<?> response = service.submit("/api/effectif-alpha", 1, "SAISIE_DONNEES", conseiller);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(workflow.getStatut()).isEqualTo(SaisieWorkflowStatus.SOUMIS);
		assertThat(workflow.getCommentaireRetour()).isEqualTo("Corriger les totaux H/F");
	}
}
