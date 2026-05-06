package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCentreTypeInfosRequest {
    private String libelle;

    private Integer idLocalite;
    private Integer idIep;
    private Integer idNaturecentre;
    private Integer idPeriodicite;
    private Integer idAutoriteAutorisation;
    private Integer idMilieuImplentation;

    private Boolean autorisation;
    private Boolean estElectrifie;
    private Boolean aDeLeau;
    private Integer nombreVisite;

    private String localisationCentre;
    private String nomMilieuImplentation;
    private String encadreurNonMena;
    private Boolean encadrerParMena;
}

