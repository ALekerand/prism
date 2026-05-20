package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

// Champs communs aux listes CEC / CP / SIE (filtres query). Champs vides ignorés ; {@code q} = recherche plein texte.
@Getter
@Setter
public abstract class SimpleCentreListFilterBase {
	/** Recherche globale (OR sur libellé type, code centre, champs texte, + id si entier). */
	private String q;
	private Integer id;
	private Integer idLocalite;
	private Integer idPeriodicite;
	/** Filtre par circonscription (tous les IEP de la DRENA) si {@code idIep} est absent. */
	private Integer idDrena;
	private Integer idIep;
	private Integer idAutoriteAutorisation;
	private Integer idNaturecentre;
	private Integer idPromoteur;
	private String codeCentre;
	private String encadreurNonMena;
	private String localisationCentre;
	private String nomMilieuImplentation;
	private Boolean autorisation;
	private Boolean encadrerParMena;
	private Boolean estElectrifie;
	private Boolean aDeLeau;
	private Integer nombreVisite;
}
