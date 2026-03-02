package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "personnel")
public class Personnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERSONNEL", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_PERSONNEL", nullable = false)
    private NiveauPersonnel idNiveauPersonnel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_DIPLOME_PERSONNEL", nullable = false)
    private DiplomePersonnel idDiplomePersonnel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_FONCTION", nullable = false)
    private Fonction idFonction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CIVILITE", nullable = false)
    private Civilite idCivilite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Centre idCentre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_STRUCTURE_FORMATION_CERTIFICATION")
    private StructureFormationCertification idStructureFormationCertification;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_STATUT_PERSONNEL", nullable = false)
    private StatutPersonnel idStatutPersonnel;

    @Column(name = "CODE_PERSONNEL", length = 50)
    private String codePersonnel;

    @Column(name = "CERTIFIER_PERSONNEL")
    private Boolean certifierPersonnel;

    @Column(name = "NOM_PERSONNEL", length = 50)
    private String nomPersonnel;

    @Column(name = "PRENOMS_PERSONNEL", length = 100)
    private String prenomsPersonnel;

    @Column(name = "ANNE_EXPE_PERSONNEL")
    private Integer anneExpePersonnel;

    @Column(name = "SEXE_PERSONNEL", length = 10)
    private String sexePersonnel;

    @Column(name = "DATE_NAISSANCE_")
    private LocalDate dateNaissance;

    @Column(name = "ANCIENNE_FONCT_PROMO_PESONNEL")
    private Integer ancienneFonctPromoPesonnel;

    @Column(name = "CONTACT_PERSONNEL", length = 20)
    private String contactPersonnel;

    @Column(name = "BOITE_POSTALE_PERSONNEL", length = 20)
    private String boitePostalePersonnel;

    @Column(name = "EMAIL_PERSONNEL", length = 100)
    private String emailPersonnel;

    @Column(name = "DENOMINATION_PERSONNEL_", length = 100)
    private String denominationPersonnel;

    @Column(name = "NOM_DU_PRGRAMME", length = 20)
    private String nomDuPrgramme;

    @Column(name = "NOM_REPRESENTANT_LEGAL_STURCTURE", length = 50)
    private String nomRepresentantLegalSturcture;


}