package com.dcspa.prism.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CentreCreatePayload {
    private Integer localiteId;
    private Integer periodiciteId;
    private Integer iepId;
    private Integer autoriteAutorisationId;
    private Integer natureCentreId;
    private Integer idMilieuImplentation;

    private String codeCentre;
    private Boolean autorisation;
    private String encadreurNonMena;
    private Boolean encadrerParMena;
    private Boolean estElectrifie;

    /** JSON stable : {@code aDeLeau} (alias historique {@code adeLeau}, déduit du getter Lombok). */
    @JsonProperty("aDeLeau")
    @JsonAlias({"adeLeau"})
    private Boolean aDeLeau;
    private Integer nombreVisite;

    private String localisationCentre;
    private String nomMilieuImplentation;
}

