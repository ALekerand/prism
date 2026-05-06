package com.dcspa.prism.dto;

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
    private Boolean aDeLeau;
    private Integer nombreVisite;

    private String localisationCentre;
    private String nomMilieuImplentation;
}

