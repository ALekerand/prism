package com.dcspa.prism.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dcspa.prism.entity.SaisieWorkflow;
import com.dcspa.prism.entity.SaisieWorkflowStatus;
import com.dcspa.prism.security.AuthUser;
import java.util.List;
import org.junit.jupiter.api.Test;

class SaisieWorkflowQueueTabResolverTest {

	@Test
	void conseiller_brouillon_est_a_soumettre() {
		SaisieWorkflow w = workflow(SaisieWorkflowStatus.BROUILLON, "c1", "c1");
		AuthUser user = user("c1", "CONSEILLER");
		assertEquals(SaisieWorkflowListTab.ACTION, SaisieWorkflowQueueTabResolver.resolve(w, user));
	}

	@Test
	void conseiller_soumis_est_en_attente() {
		SaisieWorkflow w = workflow(SaisieWorkflowStatus.SOUMIS, "c1", "c1");
		AuthUser user = user("c1", "CONSEILLER");
		assertEquals(SaisieWorkflowListTab.EN_COURS, SaisieWorkflowQueueTabResolver.resolve(w, user));
	}

	@Test
	void coordonnateur_soumis_est_a_valider() {
		SaisieWorkflow w = workflow(SaisieWorkflowStatus.SOUMIS, "c1", "c1");
		AuthUser user = user("coord", "COORDONNATEUR");
		assertEquals(SaisieWorkflowListTab.ACTION, SaisieWorkflowQueueTabResolver.resolve(w, user));
	}

	@Test
	void coordonnateur_deja_valide_par_moi() {
		SaisieWorkflow w = workflow(SaisieWorkflowStatus.VALIDEE_COORDONNATEUR, "c1", "c1");
		w.setValideCoordPar("coord");
		AuthUser user = user("coord", "COORDONNATEUR");
		assertEquals(SaisieWorkflowListTab.TERMINE, SaisieWorkflowQueueTabResolver.resolve(w, user));
	}

	@Test
	void superviseur_avant_son_tour_est_a_venir() {
		SaisieWorkflow w = workflow(SaisieWorkflowStatus.SOUMIS, "c1", "c1");
		AuthUser user = user("sup", "SUPERVISEUR");
		assertEquals(SaisieWorkflowListTab.EN_COURS, SaisieWorkflowQueueTabResolver.resolve(w, user));
	}

	@Test
	void conseiller_retourne_est_renvoye() {
		SaisieWorkflow w = workflow(SaisieWorkflowStatus.RETOURNE, "c1", "c1");
		AuthUser user = user("c1", "CONSEILLER");
		assertEquals(SaisieWorkflowListTab.RENVOYE, SaisieWorkflowQueueTabResolver.resolve(w, user));
	}

	private static SaisieWorkflow workflow(SaisieWorkflowStatus statut, String prop, String soumis) {
		SaisieWorkflow w = new SaisieWorkflow();
		w.setStatut(statut);
		w.setProprietaire(prop);
		w.setSoumisPar(soumis);
		return w;
	}

	private static AuthUser user(String username, String... roles) {
		return new AuthUser(1, username, "", true, List.of(), List.of(roles), null, null, null, null, null, null, null);
	}
}
