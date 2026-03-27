package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocaliteDImplantationRequest {
    private Integer idSousPrefecture;
    private Integer idMilieuImplentation;
    private Integer idCommune;
    private String codeLocalite;
    private String nomLocalite;
}

