package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_abandon_alpha")
public class EffectifAbandonAlpha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_ABANDON_ALPHA", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PERIODE_ACTIVITE", nullable = false)
    private PeriodeActivite idPeriodeActivite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Alpha idCentre;

    @Column(name = "CODE_EFFECTIF_ABANDON_ALPHA", length = 10)
    private String codeEffectifAbandonAlpha;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_NIVEAU_HOMME")
    private Integer effectifAbandonAlphaNiveauHomme;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_NIVEAU_FEMME")
    private Integer effectifAbandonAlphaNiveauFemme;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_MOINS_15_F")
    private Integer effectifAbandonAlphaMoins15F;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_MOINS_15_H")
    private Integer effectifAbandonAlphaMoins15H;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_MOINS_15_IVOIRIEN_H")
    private Integer effectifAbandonAlphaMoins15IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_MOINS_15_IVOIRIEN_F")
    private Integer effectifAbandonAlphaMoins15IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_MOINS_15_HANDICAP_H")
    private Integer effectifAbandonAlphaMoins15HandicapH;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_MOINS_15_HANDICAP_F")
    private Integer effectifAbandonAlphaMoins15HandicapF;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_15_24_F")
    private Integer effectifAbandonAlpha1524F;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_15_24_H")
    private Integer effectifAbandonAlpha1524H;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_15_24_IVOIRIEN_H")
    private Integer effectifAbandonAlpha1524IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_15_24_IVOIRIEN_F")
    private Integer effectifAbandonAlpha1524IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_15_24_HANDICAP_H")
    private Integer effectifAbandonAlpha1524HandicapH;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_15_24_HANDICAP_F")
    private Integer effectifAbandonAlpha1524HandicapF;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_25_49_F")
    private Integer effectifAbandonAlpha2549F;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_25_49_H")
    private Integer effectifAbandonAlpha2549H;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_25_49_IVOIRIEN_F")
    private Integer effectifAbandonAlpha2549IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_25_49_IVOIRIEN_H")
    private Integer effectifAbandonAlpha2549IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_25_49_HANDICAP_H")
    private Integer effectifAbandonAlpha2549HandicapH;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_25_49_HANDICAP_F")
    private Integer effectifAbandonAlpha2549HandicapF;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_50_PLUS_F")
    private Integer effectifAbandonAlpha50PlusF;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_50_PLUS_H")
    private Integer effectifAbandonAlpha50PlusH;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_50_PLUS_IVOIRIEN_H")
    private Integer effectifAbandonAlpha50PlusIvoirienH;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_50_PLUS_IVOIRIEN_F")
    private Integer effectifAbandonAlpha50PlusIvoirienF;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_50_PLUS_HANDICAP_H")
    private Integer effectifAbandonAlpha50PlusHandicapH;

    @Column(name = "EFFECTIF_ABANDON_ALPHA_50_PLUS_HANDICAP_F")
    private Integer effectifAbandonAlpha50PlusHandicapF;

    @Lob
    @Column(name = "CAUSE_ABANDON_ALPHA")
    private String causeAbandonAlpha;


}