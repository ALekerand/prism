package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_situation_handicap_cec")
public class EffectifSituationHandicapCec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT13", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_SIE", nullable = false)
    private NiveauSieCec idNiveauSie;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;

    @Size(max = 10)
    @Column(name = "CODE_EFFECTIF_SITUATION_HANDICAP_CEC", length = 10)
    private String codeEffectifSituationHandicapCec;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_MOINS_3_F")
    private Integer effectifSituationHandicapCecMoins3F;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_MOINS_3_H")
    private Integer effectifSituationHandicapCecMoins3H;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_MOINS_3_IVOIRIEN_H")
    private Integer effectifSituationHandicapCecMoins3IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_MOINS_3_IVOIRIEN_F")
    private Integer effectifSituationHandicapCecMoins3IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_3_5_F")
    private Integer effectifSituationHandicapCec35F;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_3_5_H")
    private Integer effectifSituationHandicapCec35H;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_3_5_IVOIRIEN_H")
    private Integer effectifSituationHandicapCec35IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_3_5_IVOIRIEN_F")
    private Integer effectifSituationHandicapCec35IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_6_8_F")
    private Integer effectifSituationHandicapCec68F;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_6_8_H")
    private Integer effectifSituationHandicapCec68H;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_6_8_IVOIRIEN_F")
    private Integer effectifSituationHandicapCec68IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_6_8_IVOIRIEN_H")
    private Integer effectifSituationHandicapCec68IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_9_11_IVOIRIEN_H")
    private Integer effectifSituationHandicapCec911IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_9_11_IVOIRIEN_F")
    private Integer effectifSituationHandicapCec911IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_12_16_F")
    private Integer effectifSituationHandicapCec1216F;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_12_16_H")
    private Integer effectifSituationHandicapCec1216H;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_12_16_IVOIRIEN_H")
    private Integer effectifSituationHandicapCec1216IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_12_16_IVOIRIEN_F")
    private Integer effectifSituationHandicapCec1216IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CEC_NIVEAU_CEC")
    private Integer effectifSituationHandicapCecNiveauCec;


}