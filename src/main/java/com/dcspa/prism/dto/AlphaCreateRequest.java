package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlphaCreateRequest {
    private Integer centreId;
    private Integer campagneId;
    private Integer categorieCentreAlphaId;
    private Integer typeAlphaId;
    private Integer regimeAlphaId;
    private String libelleAlpha;
}

