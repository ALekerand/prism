package com.dcspa.prism.service.circonscription;

/**
 * Niveau de restriction appliqué aux listes / détails de centres (tous types).
 */
public enum CirconscriptionLevel {
	/** Aucune restriction géographique (administration ou périmètre national métier). */
	NONE,
	/** Restriction à un IEP (conseiller, coordonnateur, IEPP, etc.). */
	IEP,
	/** Restriction à une DRENA (superviseur territorial). */
	DRENA,
	/** Restriction à une région (chaîne géographique via la localité du centre). */
	REGION
}
