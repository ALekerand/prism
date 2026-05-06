package com.dcspa.prism.dto;

import com.dcspa.prism.entity.TypePromoteur;
import lombok.Getter;
import lombok.Setter;

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
    private Boolean aDeLeau;
    private Integer nombreVisite;
    private String localisationCentre;
    private String nomMilieuImplentation;
    private String encadreurNonMena;
    private Boolean encadrerParMena;
    private ReferenceDetails localite;
    private ReferenceDetails iep;
    private ReferenceDetails naturecentre;
    private ReferenceDetails periodicite;
    private ReferenceDetails autoriteAutorisation;
    private PromoteurDetails promoteur;

    @Getter
    @Setter
    public static class ReferenceDetails {
        private Integer id;
        private String code;
        private String libelle;
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
