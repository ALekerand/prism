package com.dcspa.prism.dto;

/** Périmètre géographique d’une saisie workflow (centre / IEP / DRENA). */
public record WorkflowRecordScope(Integer idIep, Integer idDrena, String enregistrePar) {
}
