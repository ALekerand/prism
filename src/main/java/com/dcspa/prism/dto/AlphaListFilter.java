package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

// Paramètres de filtre optionnels pour la liste Alpha (query string). Champs vides ignorés ; {@code q} = recherche plein texte sur plusieurs colonnes.
@Getter
@Setter
public class AlphaListFilter {
	/** Recherche globale (OR sur codes, libellés, localisation, etc. + id si entier). */
	private String q;
	private Integer id;
	private Integer idCompagne;
	private Integer idCategorieCentreAlpha;
	private Integer idTypeAlpha;
	private Integer idRegimeAlpha;
	private Integer idLocalite;
	private Integer idPeriodicite;
	/** Filtre par circonscription (tous les IEP de la DRENA) si {@code idIep} est absent. */
	private Integer idDrena;
	private Integer idIep;
	private Integer idAutoriteAutorisation;
	private Integer idNaturecentre;
	private Integer idPromoteur;
	private String codeCentre;
	private String codeAlpha;
	private String libelleAlpha;
	private String encadreurNonMena;
	private String localisationCentre;
	private String nomMilieuImplentation;
	private Boolean autorisation;
	private Boolean encadrerParMena;
	private Boolean estElectrifie;
	private Boolean aDeLeau;
	private Integer nombreVisite;

	/** Aucun critère utile : la liste peut utiliser un chemin SQL allégé (projection DTO). */
	public boolean isEmpty() {
		if (q != null && !q.isBlank()) {
			return false;
		}
		return id == null
				&& idCompagne == null
				&& idCategorieCentreAlpha == null
				&& idTypeAlpha == null
				&& idRegimeAlpha == null
				&& idLocalite == null
				&& idPeriodicite == null
				&& idDrena == null
				&& idIep == null
				&& idAutoriteAutorisation == null
				&& idNaturecentre == null
				&& idPromoteur == null
				&& codeCentre == null
				&& codeAlpha == null
				&& libelleAlpha == null
				&& encadreurNonMena == null
				&& localisationCentre == null
				&& nomMilieuImplentation == null
				&& autorisation == null
				&& encadrerParMena == null
				&& estElectrifie == null
				&& aDeLeau == null
				&& nombreVisite == null;
	}
}
