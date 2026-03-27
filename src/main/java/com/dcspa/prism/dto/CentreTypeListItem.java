package com.dcspa.prism.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CentreTypeListItem {
    private Integer idCentre;
    private String codeCentre;
    private String codeType;
    private String libelle;

    // Champs "centre" utiles pour l'affichage (stockés dans les tables typées)
    private Integer idLocalite;
    private Integer idIep;
    private Integer idNaturecentre;
    private Integer idPeriodicite;
    private Integer idAutoriteAutorisation;
    private Boolean autorisation;
    private Boolean estElectrifie;
    private Boolean aDeLeau;
    private Integer nombreVisite;
    private String localisationCentre;
    private String nomMilieuImplentation;
}

