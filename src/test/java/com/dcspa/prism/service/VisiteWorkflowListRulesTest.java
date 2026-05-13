package com.dcspa.prism.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.Visite;
import com.dcspa.prism.security.AuthUser;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class VisiteWorkflowListRulesTest {

	private static AuthUser user(String username, String role, Integer idIep) {
		return new AuthUser(
				1,
				username,
				"x",
				true,
				List.of(),
				List.of(role),
				null,
				null,
				idIep,
				null,
				null,
				null,
				null);
	}

	private static Visite visiteAvecIep(int id, int idIep) {
		Alpha alpha = new Alpha();
		alpha.setIdIep(idIep);
		Visite v = new Visite();
		v.setId(id);
		v.setIdAlpha(alpha);
		return v;
	}

	private static Map<String, Object> workflowSoumisPar(String proprietaire, String soumisPar) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("workflowStatut", "SOUMIS");
		m.put("workflowProprietaire", proprietaire);
		m.put("workflowSoumisPar", soumisPar);
		return m;
	}

	@Test
	void conseiller2NeVoitPasLaSoumissionDuConseiller1() {
		Visite v = visiteAvecIep(1, 10);
		AuthUser c2 = user("commission_1_conseiller_2", "CONSEILLER", null);
		Map<String, Object> wf = workflowSoumisPar("commission_1_conseiller_1", "commission_1_conseiller_1");
		assertThat(VisiteWorkflowListRules.isRowVisible(v, wf, c2)).isFalse();
	}

	@Test
	void conseillerVoitSaPropreSoumission() {
		Visite v = visiteAvecIep(2, 10);
		AuthUser c1 = user("commission_1_conseiller_1", "CONSEILLER", null);
		Map<String, Object> wf = workflowSoumisPar("commission_1_conseiller_1", "commission_1_conseiller_1");
		assertThat(VisiteWorkflowListRules.isRowVisible(v, wf, c1)).isTrue();
	}

	@Test
	void coordinateurVoitSoumissionMemeIep() {
		Visite v = visiteAvecIep(3, 10);
		AuthUser coord = user("coord", "COORDONNATEUR", 10);
		Map<String, Object> wf = workflowSoumisPar("alice", "alice");
		assertThat(VisiteWorkflowListRules.isRowVisible(v, wf, coord)).isTrue();
	}

	@Test
	void coordinateurNeVoitPasAutreIep() {
		Visite v = visiteAvecIep(4, 99);
		AuthUser coord = user("coord", "COORDONNATEUR", 10);
		Map<String, Object> wf = workflowSoumisPar("alice", "alice");
		assertThat(VisiteWorkflowListRules.isRowVisible(v, wf, coord)).isFalse();
	}
}
