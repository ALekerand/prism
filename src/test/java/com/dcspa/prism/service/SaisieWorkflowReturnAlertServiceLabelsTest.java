package com.dcspa.prism.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SaisieWorkflowReturnAlertServiceLabelsTest {

	@Test
	void resourceLabelDistingueLesTypesEffectif() {
		assertEquals("Effectif abandon (Alpha)", SaisieWorkflowReturnAlertService.resourceLabel("/api/effectif-abandon-alpha"));
		assertEquals("Évaluation périodique", SaisieWorkflowReturnAlertService.resourceLabel("/api/evaluation"));
	}
}