package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_cepe_cp")
public class EffectifCepeCp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT18", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Cp idCentre;

    @Column(name = "CODE_EFFECTIF_CEPE_CP", length = 10)
    private String codeEffectifCepeCp;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_F_CP")
    private Integer effectifCepeCandidatFCp;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_H_CP")
    private Integer effectifCepeCandidatHCp;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_IVOIRIEN_CP")
    private Integer effectifCepeCandidatIvoirienCp;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_HANDICAP_F_CP")
    private Integer effectifCepeCandidatHandicapFCp;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_HANDICAP_H_CP")
    private Integer effectifCepeCandidatHandicapHCp;

    @Column(name = "EFFECTIF_CEPE_ADMIS_F_CP")
    private Integer effectifCepeAdmisFCp;

    @Column(name = "EFFECTIF_CEPE_ADMIS_H_CP")
    private Integer effectifCepeAdmisHCp;

    @Column(name = "EFFECTIF_CEPE_ADMIS_IVOIRIEN_CP")
    private Integer effectifCepeAdmisIvoirienCp;

    @Column(name = "EFFECTIF_CEPE_ADMIS_HANDICAP_F_CP")
    private Integer effectifCepeAdmisHandicapFCp;

    @Column(name = "EFFECTIF_CEPE_ADMIS_HANDICAP_H_CP")
    private Integer effectifCepeAdmisHandicapHCp;


}