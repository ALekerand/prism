package com.dcspa.prism.dto;

/** Périmètre auteur d’un centre : conseiller ({@code enregistrePar}) et IEP (coordonnateur). */
public record CentreAuteurScope(Integer idCentre, String enregistrePar, Integer idIep) {
}
