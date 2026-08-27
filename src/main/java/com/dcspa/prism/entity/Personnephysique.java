package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "personnephysique")
public class Personnephysique {
    @Id
    @Column(name = "ID_PROMOTEUR", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROMOTEUR", nullable = false)
    private Promoteur promoteur;

    @Size(max = 50)
    @Column(name = "CODE_PROMOTEUR", length = 50)
    private String codePromoteur;

    @Size(max = 100)
    @Column(name = "LIBELLE_PROMOTEUR", length = 100)
    private String libellePromoteur;

    @Size(max = 100)
    @Column(name = "LIBELLE_PERSONNE_PHYSIQUE", length = 100)
    private String libellePersonnePhysique;

    @Size(max = 100)
    @Column(name = "NOM", length = 100)
    private String nom;

    @Size(max = 100)
    @Column(name = "PRENOM", length = 100)
    private String prenom;

    @Size(max = 20)
    @Column(name = "CONTACT", length = 20)
    private String contact;

    @Size(max = 100)
    @Column(name = "FONCTION", length = 100)
    private String fonction;

    @Size(max = 30)
    @Column(name = "SEXE", length = 30)
    private String sexe;

    @Column(name = "DATE_NAISSANCE")
    private LocalDate dateNaissance;

    @Size(max = 100)
    @Column(name = "ANCIENNETE", length = 100)
    private String anciennete;

    @Size(max = 100)
    @Column(name = "BOITE_POSTALE", length = 100)
    private String boitePostale;

    @Size(max = 150)
    @Column(name = "NIVEAU_ETUDES", length = 150)
    private String niveauEtudes;

    @Size(max = 30)
    @Column(name = "CIVILITE", length = 30)
    private String civilite;

    @Size(max = 150)
    @Column(name = "MAIL", length = 150)
    private String mail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ORGANISATION_FAITIERE")
    private OrganisationFaitiere idOrganisationFaitiere;

}