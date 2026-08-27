package com.dcspa.prism.dto;

import com.dcspa.prism.entity.TypePromoteur;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CentreWithPromoteurItem {
    private Integer idCentre;
    private String codeCentre;
    private String codeType;
    private String libelle;
    private Integer idLocalite;
    private Integer idIep;
    private Integer idNaturecentre;
    private Integer idPeriodicite;
    private Integer idAutoriteAutorisation;
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

    /** {@code false} = centre exclu des statistiques. */
    private Boolean actif;

    /** Détail CEC : école tutrice (autres types : {@code null}). */
    private String ecoleTutrice;

    /** Détail CEC : année de création (autres types : {@code null}). */
    private Integer anneeCreation;

    private ReferenceDetails localite;
    private ReferenceDetails iep;
    private ReferenceDetails drena;
    private ReferenceDetails commune;
    private ReferenceDetails sousPrefecture;
    private ReferenceDetails departement;
    private ReferenceDetails region;
    private ReferenceDetails naturecentre;
    private ReferenceDetails periodicite;
    private ReferenceDetails autoriteAutorisation;
    private PromoteurDetails promoteur;

    /** Renseignés uniquement pour le détail Alpha. */
    private Integer idCompagne;
    private Integer idCategorieCentreAlpha;
    private Integer idTypeAlpha;
    private Integer idRegimeAlpha;

    private ReferenceDetails campagne;
    private ReferenceDetails categorieCentreAlpha;
    private ReferenceDetails typeAlpha;
    private ReferenceDetails regimeAlpha;

    /** Renseignés uniquement pour le détail SIE. */
    private Integer idTypeSie;
    private ReferenceDetails typeSie;

    private List<NiveauDetails> niveaux;

    @Getter
    @Setter
    public static class ReferenceDetails {
        private Integer id;
        private String code;
        private String libelle;
    }

    @Getter
    @Setter
    public static class NiveauDetails {
        private Integer id;
        private Integer niveauId;
        private String codeNiveau;
        private String libelleNiveau;
        private ReferenceDetails anneeScolaire;
        private Integer nombreSalle;
    }

    @Getter
    @Setter
    public static class PromoteurDetails {
        private Integer idPromoteur;
        private String codePromoteur;
        private String libellePromoteur;
        private TypePromoteur typePromoteur;
        private PersonnePhysiqueDetails personnePhysique;
        private PersonneMoraleDetails personneMorale;
    }

    @Getter
    @Setter
    public static class PersonnePhysiqueDetails {
        private String libellePersonnePhysique;
        private String nom;
        private String prenom;
        private String contact;
        private String fonction;
        private String sexe;
        private LocalDate dateNaissance;
        private String anciennete;
        private String boitePostale;
        private String niveauEtudes;
        private String civilite;
    }

    @Getter
    @Setter
    public static class PersonneMoraleDetails {
        private String denomination;
        private String nomProgramme;
        private String nomRepresentant;
        private String contact;
        private String boitePostale;
        private String mail;
        private Integer idTypePersonneMorale;
        private String libelleTypePersonneMorale;
    }
}
