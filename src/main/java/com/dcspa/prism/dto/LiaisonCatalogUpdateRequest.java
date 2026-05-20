package com.dcspa.prism.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiaisonCatalogUpdateRequest {
    private Integer liaisonId;
    private Integer catalogId;
    private String libelleAutreInfrastructure;
    private String libelleAutreMateriel;
    private String sourceFinancement;
    private BigDecimal montant;
}
