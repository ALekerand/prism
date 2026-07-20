package com.dcspa.prism.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CentreTypeListItem {
    private Integer idCentre;
    private String codeCentre;
    private String codeType;
    private String libelle;

    // Champs "centre" utiles pour l'affichage (stockés dans les tables typées)
    private Integer idLocalite;
    private Integer idIep;
    private Integer idNaturecentre;
    private Integer idPeriodicite;
    private Integer idAutoriteAutorisation;
    private Boolean autorisation;
    private Boolean estElectrifie;

    /**
     * Getter explicite : Lombok {@code getADeLeau()} sérialisait aussi {@code ADeLeau}
     * en plus de {@code aDeLeau}, ce qui casse les parsers JSON stricts.
     */
    @Getter(AccessLevel.NONE)
    private Boolean aDeLeau;

    @JsonProperty("aDeLeau")
    @JsonAlias({"adeLeau", "ADeLeau"})
    public Boolean getADeLeau() {
        return aDeLeau;
    }

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

    /** FK promoteur (liste / filtres UI). */
    private Integer idPromoteur;

    /** Actif pour les statistiques ({@code false} = inactif). */
    private Boolean actif;
}

