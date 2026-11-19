package com.dcspa.prism.service;

import com.dcspa.prism.entity.SaisieWorkflow;
import com.dcspa.prism.entity.SaisieWorkflowStatus;
import com.dcspa.prism.security.AuthUser;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SaisieWorkflowReturnNotificationRulesTest {

	@Test
	void conseillerEstNotifieSurRetour() {
		SaisieWorkflow w = workflow(SaisieWorkflowStatus.RETOURNE, "cons1", "cons1", "coord", null);
		AuthUser user = user("cons1", "CONSEILLER");
		assertTrue(SaisieWorkflowReturnNotificationRules.shouldNotify(user, w));
	}

	@Test
	void coordNonNotifieQuandIlARenvoyeLuiMeme() {
		SaisieWorkflow w = workflow(SaisieWorkflowStatus.RETOURNE, "cons1", "cons1", "coord", null);
		AuthUser user = user("coord", "COORDONNATEUR");
		assertFalse(SaisieWorkflowReturnNotificationRules.shouldNotifyCoordonnateur(w, user, true));
	}

	@Test
	void coordNotifieQuandSuperieurRenvoieApresValidationCoord() {
		SaisieWorkflow w = workflow(SaisieWorkflowStatus.RETOURNE, "cons1", "cons1", "sup1", "coord");
		AuthUser user = user("coord", "COORDONNATEUR");
		assertTrue(SaisieWorkflowReturnNotificationRules.shouldNotifyCoordonnateur(w, user, true));
	}

	@Test
	void coordNotifieQuandSuperieurRenvoieAvantValidationCoord() {
		SaisieWorkflow w = workflow(SaisieWorkflowStatus.RETOURNE, "cons1", "cons1", "sup1", null);
		AuthUser user = user("coord", "COORDONNATEUR");
		assertTrue(SaisieWorkflowReturnNotificationRules.shouldNotifyCoordonnateur(w, user, true));
	}

	private static SaisieWorkflow workflow(
			SaisieWorkflowStatus statut,
			String prop,
			String soumis,
			String decidePar,
			String valideCoordPar) {
		SaisieWorkflow w = new SaisieWorkflow();
		w.setStatut(statut);
		w.setProprietaire(prop);
		w.setSoumisPar(soumis);
		w.setDecidePar(decidePar);
		w.setValideCoordPar(valideCoordPar);
		return w;
	}

	private static AuthUser user(String username, String... roles) {
		return new AuthUser(1, username, "", true, List.of(), List.of(roles), null, null, null, null, null, null, null);
	}
}
