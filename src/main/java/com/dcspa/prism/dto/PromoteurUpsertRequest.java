package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromoteurUpsertRequest {
    /**
     * Si renseigné, le promoteur existant sera réutilisé.
     * Sinon un nouveau promoteur sera créé avec les champs ci-dessous.
     */
    private Integer id;

    private String codePromoteur;
    private String libellePromoteur;
}

