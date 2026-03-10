package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_cp")
public class EffectifCp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT14", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_CP", nullable = false)
    private NiveauCp idNiveauCp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Cp idCentre;

    @Column(name = "CODE_EFFECTIF_CP", length = 10)
    private String codeEffectifCp;

    @Column(name = "EFFECTIF_CP_9_11_IVOIRIEN_H")
    private Integer effectifCp911IvoirienH;

    @Column(name = "EFFECTIF_CP_9_11_IVOIRIEN_F")
    private Integer effectifCp911IvoirienF;

    @Column(name = "EFFECTIF_CP_9_11_HANDICAP_H")
    private Integer effectifCp911HandicapH;

    @Column(name = "EFFECTIF_CP_9_11_HANDICAP_F")
    private Integer effectifCp911HandicapF;

    @Column(name = "EFFECTIF_CP_9_11_NON_IVOIRIEN_F")
    private Integer effectifCp911NonIvoirienF;

    @Column(name = "EFFECTIF_CP_9_11_NON_IVOIRIEN_H")
    private Integer effectifCp911NonIvoirienH;

    @Column(name = "EFFECTIF_CP_12_13_IVOIRIEN_F")
    private Integer effectifCp1213IvoirienF;

    @Column(name = "EFFECTIF_CP_12_13_IVOIRIEN_H")
    private Integer effectifCp1213IvoirienH;

    @Column(name = "EFFECTIF_CP_12_13_HANDICAP_H")
    private Integer effectifCp1213HandicapH;

    @Column(name = "EFFECTIF_CP_12_13_HANDICAP_F")
    private Integer effectifCp1213HandicapF;

    @Column(name = "EFFECTIF_CP_12_13_NON_IVOIRIIEN_H")
    private Integer effectifCp1213NonIvoiriienH;

    @Column(name = "EFFECTIF_CP_14_IVOIRIEN_H")
    private Integer effectifCp14IvoirienH;

    @Column(name = "EFFECTIF_CP_14_IVOIRIEN_F")
    private Integer effectifCp14IvoirienF;

    @Column(name = "EFFECTIF_CP_14_HANDICAP_H")
    private Integer effectifCp14HandicapH;

    @Column(name = "EFFECTIF_CP_14_HANDICAP_F")
    private Integer effectifCp14HandicapF;

    @Column(name = "EFFECTIF_CP_14_NON_IVOIRIEN_F")
    private Integer effectifCp14NonIvoirienF;

    @Column(name = "EFFECTIF_CP_14_NON_IVOIRIEN_H")
    private Integer effectifCp14NonIvoirienH;

    @Column(name = "EFFECTIF_CP_NIVEAU_CP")
    private Integer effectifCpNiveauCp;


}