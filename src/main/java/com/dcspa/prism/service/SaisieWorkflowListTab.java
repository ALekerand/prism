package com.dcspa.prism.service;

/** Onglet de liste pour les données soumises à validation (vue utilisateur). */
public enum SaisieWorkflowListTab {
	/** À soumettre (conseiller) ou à valider maintenant (validateur). */
	ACTION,
	/** Soumis en attente (conseiller) ou pas encore au palier de l'utilisateur (validateur). */
	EN_COURS,
	/** Validé / traité par l'utilisateur ou circuit terminé côté conseiller. */
	TERMINE
}
