package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_situation_handicap_alpha")
public class EffectifSituationHandicapAlpha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_SITUATION_HANDICAP_ALPHA", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PERIODE_ACTIVITE", nullable = false)
    private PeriodeActivite idPeriodeActivite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Alpha idCentre;

    @Column(name = "CODE_EFFECTIF_SITUATION_HANDICAP_ALPHA", length = 10)
    private String codeEffectifSituationHandicapAlpha;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_NIVEAU_HOMME")
    private Integer effectifSituationHandicapAlphaNiveauHomme;

    @Column(name = "EFFECTIF_SITUATION_HANDICAPALPHA_NIVEAU_FEMME")
    private Integer effectifSituationHandicapalphaNiveauFemme;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_MOINS_15_F")
    private Integer effectifSituationHandicapAlphaMoins15F;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_MOINS_15_H")
    private Integer effectifSituationHandicapAlphaMoins15H;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_MOINS_15_IVOIRIEN_H")
    private Integer effectifSituationHandicapAlphaMoins15IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP__ALPHA_MOINS_15_IVOIRIEN_F")
    private Integer effectifSituationHandicapAlphaMoins15IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP__ALPHA_MOINS_15_HANDICAP_H")
    private Integer effectifSituationHandicapAlphaMoins15HandicapH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAPALPHA_MOINS_15_HANDICAP_F")
    private Integer effectifSituationHandicapalphaMoins15HandicapF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_15_24_F")
    private Integer effectifSituationHandicapAlpha1524F;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_15_24_H")
    private Integer effectifSituationHandicapAlpha1524H;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_15_24_IVOIRIEN_H")
    private Integer effectifSituationHandicapAlpha1524IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_15_24_IVOIRIEN_F")
    private Integer effectifSituationHandicapAlpha1524IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_15_24_HANDICAP_H")
    private Integer effectifSituationHandicapAlpha1524HandicapH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_15_24_HANDICAP_F")
    private Integer effectifSituationHandicapAlpha1524HandicapF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_25_49_F")
    private Integer effectifSituationHandicapAlpha2549F;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_25_49_H")
    private Integer effectifSituationHandicapAlpha2549H;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_25_49_IVOIRIEN_F")
    private Integer effectifSituationHandicapAlpha2549IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_25_49_IVOIRIEN_H")
    private Integer effectifSituationHandicapAlpha2549IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_25_49_HANDICAP_H")
    private Integer effectifSituationHandicapAlpha2549HandicapH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_25_49_HANDICAP_F")
    private Integer effectifSituationHandicapAlpha2549HandicapF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_50_PLUS_F")
    private Integer effectifSituationHandicapAlpha50PlusF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_50_PLUS_H")
    private Integer effectifSituationHandicapAlpha50PlusH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_50_PLUS_IVOIRIEN_H")
    private Integer effectifSituationHandicapAlpha50PlusIvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_50_PLUS_IVOIRIEN_F")
    private Integer effectifSituationHandicapAlpha50PlusIvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_50_PLUS_HANDICAP_H")
    private Integer effectifSituationHandicapAlpha50PlusHandicapH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_ALPHA_50_PLUS_HANDICAP_F")
    private Integer effectifSituationHandicapAlpha50PlusHandicapF;


}