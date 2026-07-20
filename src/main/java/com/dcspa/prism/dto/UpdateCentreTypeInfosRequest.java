package com.dcspa.prism.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateCentreTypeInfosRequest {
    private String libelle;

    private Integer idLocalite;
    private Integer idIep;
    private Integer idNaturecentre;
    private Integer idPeriodicite;
    private Integer idAutoriteAutorisation;
    private Integer idMilieuImplentation;

    private Boolean autorisation;
    private Boolean estElectrifie;

    @JsonProperty("aDeLeau")
    @JsonAlias({"adeLeau"})
    private Boolean aDeLeau;
    private Integer nombreVisite;

    private Integer totalApprenants;
    private Integer totalHommes;
    private Integer totalFemmes;
    private String latitudeGps;
    private String longitudeGps;
    private Boolean gpsValide;
    private String structurePartenaire;
    private String nomPartenaire;

    private String localisationCentre;
    private String nomMilieuImplentation;
    private String encadreurNonMena;
    private Boolean encadrerParMena;

    /** Si présent, met à jour le promoteur lié au centre sous-jacent. */
    private Integer idPromoteur;

    /** Renseigné pour les fiches CEC uniquement. */
    private String ecoleTutrice;

    /** Année civile de création (CEC). */
    private Integer anneeCreation;

    /** Remplacement complet des niveaux Alpha si le champ est présent. */
    private List<AlphaNiveauCreatePayload> niveauxAlpha;

    /** Remplacement complet des niveaux CP/CEC/SIE si le champ est présent. */
    private List<CentreTypeNiveauCreatePayload> niveaux;

    /** Inclus dans les statistiques si {@code true} ou {@code null}. */
    private Boolean actif;
}

