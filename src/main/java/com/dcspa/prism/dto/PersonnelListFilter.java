package com.dcspa.prism.dto;

import lombok.Data;

/**
 * Filtres optionnels pour la liste paginée du personnel d’un centre (paramètres de requête).
 */
@Data
public class PersonnelListFilter {
	private Integer idFonction;
	private Integer idStatutPersonnel;
	private Integer idNiveauPersonnel;
	private Integer idCivilite;
	/** Filtre exact (ex. M / F) ; vide ignoré. */
	private String sexePersonnel;
	/**
	 * Recherche texte : nom, prénoms, contact, email, code personnel (OR, insensible à la casse) ;
	 * si la valeur est un entier, correspond aussi à l’identifiant fiche.
	 */
	private String q;
}
