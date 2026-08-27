package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SimpleCentreTypeFullCreateRequest {
    private String libelle;

    /** Métadonnée réservée au type CEC (ignorée pour CP / SIE). */
    private String ecoleTutrice;

    /** Année civile de création du CEC (ignorée pour CP / SIE). */
    private Integer anneeCreation;

    /** Type SIE (référentiel {@code type_sie}, obligatoire pour SIE). */
    private Integer typeSieId;

    private PromoteurUpsertRequest promoteur;
    private CentreCreatePayload centre;
    private List<CentreTypeNiveauCreatePayload> niveaux;
}

