package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CentreRequest {
    private Integer idPromoteur;
    private Integer idPeriodicite;
    private Integer idAutoriteAutorisation;

    private String codeCentre;
    private Boolean autorisation;
    private String encadreurNonMena;
    private Boolean encadrerParMena;
    private Boolean estElectrifie;
    private Boolean aDeLeau;
    private Integer nombreVisite;
}

