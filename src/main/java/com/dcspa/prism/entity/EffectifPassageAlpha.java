package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_passage_alpha")
public class EffectifPassageAlpha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_PASSAGE_ALPHA", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PERIODE_ACTIVITE", nullable = false)
    private PeriodeActivite idPeriodeActivite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Alpha idCentre;

    @Column(name = "CODE_EFFECTIF_PASSAGE_ALPHA", length = 10)
    private String codeEffectifPassageAlpha;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_NIVEAU_HOMME")
    private Integer effectifPassageAlphaNiveauHomme;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_NIVEAU_FEMME")
    private Integer effectifPassageAlphaNiveauFemme;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_MOINS_15_F")
    private Integer effectifPassageAlphaMoins15F;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_MOINS_15_H")
    private Integer effectifPassageAlphaMoins15H;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_MOINS_15_IVOIRIEN_H")
    private Integer effectifPassageAlphaMoins15IvoirienH;

    @Column(name = "EFFECTIF_PASSAGE__ALPHA_MOINS_15_IVOIRIEN_F")
    private Integer effectifPassageAlphaMoins15IvoirienF;

    @Column(name = "EFFECTIF_PASSAGE__ALPHA_MOINS_15_HANDICAP_H")
    private Integer effectifPassageAlphaMoins15HandicapH;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_MOINS_15_HANDICAP_F")
    private Integer effectifPassageAlphaMoins15HandicapF;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_15_24_F")
    private Integer effectifPassageAlpha1524F;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_15_24_H")
    private Integer effectifPassageAlpha1524H;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_15_24_IVOIRIEN_H")
    private Integer effectifPassageAlpha1524IvoirienH;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_15_24_IVOIRIEN_F")
    private Integer effectifPassageAlpha1524IvoirienF;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_15_24_HANDICAP_H")
    private Integer effectifPassageAlpha1524HandicapH;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_15_24_HANDICAP_F")
    private Integer effectifPassageAlpha1524HandicapF;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_25_49_F")
    private Integer effectifPassageAlpha2549F;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_25_49_H")
    private Integer effectifPassageAlpha2549H;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_25_49_IVOIRIEN_F")
    private Integer effectifPassageAlpha2549IvoirienF;

    @Column(name = "EFFECTIF_APASSAGE_ALPHA_25_49_IVOIRIEN_H")
    private Integer effectifApassageAlpha2549IvoirienH;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_25_49_HANDICAP_H")
    private Integer effectifPassageAlpha2549HandicapH;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_25_49_HANDICAP_F")
    private Integer effectifPassageAlpha2549HandicapF;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_50_PLUS_F")
    private Integer effectifPassageAlpha50PlusF;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_50_PLUS_H")
    private Integer effectifPassageAlpha50PlusH;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_50_PLUS_IVOIRIEN_H")
    private Integer effectifPassageAlpha50PlusIvoirienH;

    @Column(name = "EFFECTIF_APASSAGE_ALPHA_50_PLUS_IVOIRIEN_F")
    private Integer effectifApassageAlpha50PlusIvoirienF;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_50_PLUS_HANDICAP_H")
    private Integer effectifPassageAlpha50PlusHandicapH;

    @Column(name = "EFFECTIF_PASSAGE_ALPHA_50_PLUS_HANDICAP_F")
    private Integer effectifPassageAlpha50PlusHandicapF;


}