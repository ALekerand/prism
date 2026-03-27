package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleCentreTypeFullCreateRequest {
    private String libelle;

    private PromoteurUpsertRequest promoteur;
    private CentreCreatePayload centre;
}

