package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_situation_handicap_sie")
public class EffectifSituationHandicapSie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT24", nullable = false)
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
    @Column(name = "CODE_EFFECTIF_SITUATION_HANDICAP_SIE", length = 10)
    private String codeEffectifSituationHandicapSie;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_3_IVOIRIEN_H")
    private Integer effectifSituationHandicapSie3IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_3_IVOIRIEN_F")
    private Integer effectifSituationHandicapSie3IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_3_NON_IVOIRIEN_F")
    private Integer effectifSituationHandicapSie3NonIvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_3_NON_IVOIRIEN_H")
    private Integer effectifSituationHandicapSie3NonIvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_4_6_IVOIRIEN_F")
    private Integer effectifSituationHandicapSie46IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_4_6_IVOIRIEN_H")
    private Integer effectifSituationHandicapSie46IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_7_9_IVOIRIEN_H")
    private Integer effectifSituationHandicapSie79IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_7_9_IVOIRIEN_F")
    private Integer effectifSituationHandicapSie79IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_7_9_NON_IVOIRIEN_F")
    private Integer effectifSituationHandicapSie79NonIvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_7_9_NON_IVOIRIEN_H")
    private Integer effectifSituationHandicapSie79NonIvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_10_12_IVOIRIEN_F")
    private Integer effectifSituationHandicapSie1012IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_10_12_IVOIRIEN_H")
    private Integer effectifSituationHandicapSie1012IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAPL_SIE_10_12_NON_IVOIRIEN_H")
    private Integer effectifSituationHandicaplSie1012NonIvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_10_12_NON_IVOIRIEN_F")
    private Integer effectifSituationHandicapSie1012NonIvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_13_14_ET_PLUS_IVOIRIEN_F")
    private Integer effectifSituationHandicapSie1314EtPlusIvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_13_14_ET_PLUS_IVOIRIEN_H")
    private Integer effectifSituationHandicapSie1314EtPlusIvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_SIE_NIVEAU_SIE")
    private Integer effectifSituationHandicapSieNiveauSie;


}