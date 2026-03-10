package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_promu_cec")
public class EffectifPromuCec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT11", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_SIE", nullable = false)
    private NiveauSieCec idNiveauSie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Cec idCentre;

    @Column(name = "CODE_EFFECTIF_PROMU_CEC", length = 10)
    private String codeEffectifPromuCec;

    @Column(name = "EFFECTIF_PROMU_CEC_MOINS_3_F")
    private Integer effectifPromuCecMoins3F;

    @Column(name = "EFFECTIF_PROMU_CEC_MOINS_3_H")
    private Integer effectifPromuCecMoins3H;

    @Column(name = "EFFECTIF_PROMU_CEC_MOINS_3_IVOIRIEN_H")
    private Integer effectifPromuCecMoins3IvoirienH;

    @Column(name = "EFFECTIF_PROMUCEC_MOINS_3_IVOIRIEN_F")
    private Integer effectifPromucecMoins3IvoirienF;

    @Column(name = "EFFECTIF_PROMU_CEC_MOINS_3_HANDICAP_H")
    private Integer effectifPromuCecMoins3HandicapH;

    @Column(name = "EFFECTIF_PROMU_CEC_MOINS_3_HANDICAP_F")
    private Integer effectifPromuCecMoins3HandicapF;

    @Column(name = "EFFECTIF_PROMU_CEC_3_5_F")
    private Integer effectifPromuCec35F;

    @Column(name = "EFFECTIF_PROMU_CEC_3_5_H")
    private Integer effectifPromuCec35H;

    @Column(name = "EFFECTIF_PROMU_CEC_3_5_IVOIRIEN_H")
    private Integer effectifPromuCec35IvoirienH;

    @Column(name = "EFFECTIF_PROMU_CEC_3_5_IVOIRIEN_F")
    private Integer effectifPromuCec35IvoirienF;

    @Column(name = "EFFECTIF_PROMU_CEC_3_5_HANDICAP_H")
    private Integer effectifPromuCec35HandicapH;

    @Column(name = "EFFECTIF_PROMU_CEC_3_5_HANDICAP_F")
    private Integer effectifPromuCec35HandicapF;

    @Column(name = "EFFECTIF_PROMU_CEC_6_8_F")
    private Integer effectifPromuCec68F;

    @Column(name = "EFFECTIF_PROMU_CEC_6_8_H")
    private Integer effectifPromuCec68H;

    @Column(name = "EFFECTIF_PROMU_CEC_6_8_IVOIRIEN_F")
    private Integer effectifPromuCec68IvoirienF;

    @Column(name = "EFFECTIF_PROMU_CEC_6_8_IVOIRIEN_H")
    private Integer effectifPromuCec68IvoirienH;

    @Column(name = "EFFECTIF_PROMU_CEC_6_8_HANDICAP_H")
    private Integer effectifPromuCec68HandicapH;

    @Column(name = "EFFECTIF_PROMU_CEC_6_8_HANDICAP_F")
    private Integer effectifPromuCec68HandicapF;

    @Column(name = "EFFECTIF_PROMU_CEC_9_11_H")
    private Integer effectifPromuCec911H;

    @Column(name = "EFFECTIF_PROMU_CEC_9_11_IVOIRIEN_H")
    private Integer effectifPromuCec911IvoirienH;

    @Column(name = "EFFECTIF_PROMU_CEC_9_11_IVOIRIEN_F")
    private Integer effectifPromuCec911IvoirienF;

    @Column(name = "EFFECTIF_PROMU_CEC_9_11_HANDICAP_H")
    private Integer effectifPromuCec911HandicapH;

    @Column(name = "EFFECTIF_PROMU_CEC_9_11_HANDICAP_F")
    private Integer effectifPromuCec911HandicapF;

    @Column(name = "EFFECTIF_PROMU_CEC_12_16_F")
    private Integer effectifPromuCec1216F;

    @Column(name = "EFFECTIF_PROMU_CEC_12_16_H")
    private Integer effectifPromuCec1216H;

    @Column(name = "EFFECTIF_PROMU_CEC_12_16_IVOIRIEN_H")
    private Integer effectifPromuCec1216IvoirienH;

    @Column(name = "EFFECTIF_PROMU_CEC_12_16_IVOIRIEN_F")
    private Integer effectifPromuCec1216IvoirienF;

    @Column(name = "EFFECTIF_PROMU_CEC_12_16_HANDICAP_H")
    private Integer effectifPromuCec1216HandicapH;

    @Column(name = "EFFECTIF_PROMU_CEC_12_16_HANDICAP_F")
    private Integer effectifPromuCec1216HandicapF;

    @Column(name = "EFFECTIF_PROMU_CEC_NIVEAU_CEC")
    private Integer effectifPromuCecNiveauCec;


}