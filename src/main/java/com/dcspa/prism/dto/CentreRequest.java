package com.dcspa.prism.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("aDeLeau")
    @JsonAlias({"adeLeau"})
    private Boolean aDeLeau;
    private Integer nombreVisite;
}

