package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SimpleCentreTypeFullCreateRequest {
    private String libelle;

    private PromoteurUpsertRequest promoteur;
    private CentreCreatePayload centre;
    private List<CentreTypeNiveauCreatePayload> niveaux;
}

