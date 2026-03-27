package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlphaFullCreateRequest {
    private Integer campagneId;
    private Integer categorieCentreAlphaId;
    private Integer typeAlphaId;
    private Integer regimeAlphaId;
    private String libelleAlpha;

    private PromoteurUpsertRequest promoteur;
    private CentreCreatePayload centre;
}

