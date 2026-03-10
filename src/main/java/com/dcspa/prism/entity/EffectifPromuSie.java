package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_promu_sie")
public class EffectifPromuSie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT22", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_SIE", nullable = false)
    private NiveauSieCec idNiveauSie;

    @Column(name = "CODE_EFFECTIF_PROMU_SIE", length = 10)
    private String codeEffectifPromuSie;

    @Column(name = "EFFECTIF_PROMU_SIE_3_IVOIRIEN_H")
    private Integer effectifPromuSie3IvoirienH;

    @Column(name = "EFFECTIF_PROMU_SIE_3_IVOIRIEN_F")
    private Integer effectifPromuSie3IvoirienF;

    @Column(name = "EFFECTIF_PROMU_SIE_3_HANDICAP_H")
    private Integer effectifPromuSie3HandicapH;

    @Column(name = "EFFECTIF_PROMU_SIE_3_HANDICAP_F")
    private Integer effectifPromuSie3HandicapF;

    @Column(name = "EFFECTIF_PROMU_SIE_3_NON_IVOIRIEN_F")
    private Integer effectifPromuSie3NonIvoirienF;

    @Column(name = "EFFECTIF_PROMU_SIE_3_NON_IVOIRIEN_H")
    private Integer effectifPromuSie3NonIvoirienH;

    @Column(name = "EFFECTIF_PROMU_SIE_4_6_IVOIRIEN_F")
    private Integer effectifPromuSie46IvoirienF;

    @Column(name = "EFFECTIF_PROMU_SIE_4_6_IVOIRIEN_H")
    private Integer effectifPromuSie46IvoirienH;

    @Column(name = "EFFECTIF_PROMU_SIE_4_6_HANDICAP_H")
    private Integer effectifPromuSie46HandicapH;

    @Column(name = "EFFECTIF_PROMU_SIE_4_6_HANDICAP_F")
    private Integer effectifPromuSie46HandicapF;

    @Column(name = "EFFECTIF_PROMU_SIE_4_6_NON_IVOIRIIEN_H")
    private Integer effectifPromuSie46NonIvoiriienH;

    @Column(name = "EFFECTIF_PROMU_SIE_7_9_IVOIRIEN_H")
    private Integer effectifPromuSie79IvoirienH;

    @Column(name = "EFFECTIF_PROMU_SIE_7_9_IVOIRIEN_F")
    private Integer effectifPromuSie79IvoirienF;

    @Column(name = "EFFECTIF_PROMU_SIE_7_9_HANDICAP_H")
    private Integer effectifPromuSie79HandicapH;

    @Column(name = "EFFECTIF_PROMU_SIE_7_9_HANDICAP_F")
    private Integer effectifPromuSie79HandicapF;

    @Column(name = "EFFECTIF_PROMU_SIE_7_9_NON_IVOIRIEN_F")
    private Integer effectifPromuSie79NonIvoirienF;

    @Column(name = "EFFECTIF_PROMU_SIE_7_9_NON_IVOIRIEN_H")
    private Integer effectifPromuSie79NonIvoirienH;

    @Column(name = "EFFECTIF_PROMU_SIE_10_12_IVOIRIEN_F")
    private Integer effectifPromuSie1012IvoirienF;

    @Column(name = "EFFECTIF_PROMU_SIE_10_12_IVOIRIEN_H")
    private Integer effectifPromuSie1012IvoirienH;

    @Column(name = "EFFECTIF_PROMU_SIE_10_12_HANDICAP_H")
    private Integer effectifPromuSie1012HandicapH;

    @Column(name = "EFFECTIF_PROMU_SIE_10_12_HANDICAP_F")
    private Integer effectifPromuSie1012HandicapF;

    @Column(name = "EFFECTIF_PROMU_SIE_10_12_NON_IVOIRIEN_H")
    private Integer effectifPromuSie1012NonIvoirienH;

    @Column(name = "EFFECTIF_PROMU_SIE_10_12_NON_IVOIRIEN_F")
    private Integer effectifPromuSie1012NonIvoirienF;

    @Column(name = "EFFECTIF_PROMU_SIE_13_14_ET_PLUS_IVOIRIEN_F")
    private Integer effectifPromuSie1314EtPlusIvoirienF;

    @Column(name = "EFFECTIF_PROMU_SIE_13_14_ET_PLUS_IVOIRIEN_H")
    private Integer effectifPromuSie1314EtPlusIvoirienH;

    @Column(name = "EFFECTIF_PROMU_SIE_13_14_ET_PLUS_HANDICAP_F")
    private Integer effectifPromuSie1314EtPlusHandicapF;

    @Column(name = "EFFECTIF_PROMU_SIE_13_14_ET_PLUS_HANDICAP_H")
    private Integer effectifPromuSie1314EtPlusHandicapH;

    @Column(name = "EFFECTIF_PROMU_SIE_NIVEAU_SIE")
    private Integer effectifPromuSieNiveauSie;


}