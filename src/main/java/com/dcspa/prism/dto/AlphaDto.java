package com.dcspa.prism.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlphaDto {

	private Integer id;
	private Integer idCentre;
	private Integer idCampagne;
	private Integer idTypeAlpha;
	private Integer idRegimeAlpha;
	private Integer idPeriodicite;
	private Integer idAutoriteAutorisation;
	private Integer idPromoteur;
	private String codeCentre;
	private Boolean autorisation;
	private String encadreurNonMena;
	private Boolean encadrerParMena;
	private Boolean estElectrifie;
	private Boolean aDeLeau;
	private Integer nombreVisite;
	private String codeAlpha;
	private String libelleAlpha;
}
