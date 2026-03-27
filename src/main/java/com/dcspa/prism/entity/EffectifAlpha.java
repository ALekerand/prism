package com.dcspa.prism.entity;

import com.dcspa.prism.codegen.AutoCode;
import com.dcspa.prism.codegen.AutoCodeEntityListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_alpha")
@AutoCode(field = "codeEffectifAlpha")
@EntityListeners(AutoCodeEntityListener.class)
public class EffectifAlpha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_ALPHA", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PERIODE_ACTIVITE", nullable = false)
    private PeriodeActivite idPeriodeActivite;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Alpha idCentre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_NIVEAU_ALPHA")
    private NiveauAlpha idNiveauAlpha;

    @Size(max = 10)
    @Column(name = "CODE_EFFECTIF_ALPHA", length = 10)
    private String codeEffectifAlpha;

    @Column(name = "EFFECTIF_ALPHA_NIVEAU_H")
    private Integer effectifAlphaNiveauH;

    @Column(name = "EFFECTIF_ALPHA_NIVEAU_F")
    private Integer effectifAlphaNiveauF;

    @Column(name = "EFFECTIF_ALPHA_MOINS_15_F")
    private Integer effectifAlphaMoins15F;

    @Column(name = "EFFECTIF_ALPHA_MOINS_15_H")
    private Integer effectifAlphaMoins15H;

    @Column(name = "EFFECTIF_ALPHA_MOINS_15_IVOIRIEN_H")
    private Integer effectifAlphaMoins15IvoirienH;

    @Column(name = "EFFECTIF_ALPHA_MOINS_15_IVOIRIEN_F")
    private Integer effectifAlphaMoins15IvoirienF;

    @Column(name = "EFFECTIF_ALPHA_MOINS_15_HANDICAP_H")
    private Integer effectifAlphaMoins15HandicapH;

    @Column(name = "EFFECTIF_ALPHA_MOINS_15_HANDICAP_F")
    private Integer effectifAlphaMoins15HandicapF;

    @Column(name = "EFFECTIF_ALPHA_15_24_F")
    private Integer effectifAlpha1524F;

    @Column(name = "EFFECTIF_ALPHA_15_24_H")
    private Integer effectifAlpha1524H;

    @Column(name = "EFFECTIF_ALPHA_15_24_IVOIRIEN_H")
    private Integer effectifAlpha1524IvoirienH;

    @Column(name = "EFFECTIF_ALPHA_15_24_IVOIRIEN_F")
    private Integer effectifAlpha1524IvoirienF;

    @Column(name = "EFFECTIF_ALPHA_15_24_HANDICAP_H")
    private Integer effectifAlpha1524HandicapH;

    @Column(name = "EFFECTIF_ALPHA_15_24_HANDICAP_F")
    private Integer effectifAlpha1524HandicapF;

    @Column(name = "EFFECTIF_ALPHA_25_49_F")
    private Integer effectifAlpha2549F;

    @Column(name = "EFFECTIF_ALPHA_25_49_H")
    private Integer effectifAlpha2549H;

    @Column(name = "EFFECTIF_ALPHA_25_49_IVOIRIEN_F")
    private Integer effectifAlpha2549IvoirienF;

    @Column(name = "EFFECTIF_ALPHA_25_49_IVOIRIEN_H")
    private Integer effectifAlpha2549IvoirienH;

    @Column(name = "EFFECTIF_ALPHA_25_49_HANDICAP_H")
    private Integer effectifAlpha2549HandicapH;

    @Column(name = "EFFECTIF_ALPHA_25_49_HANDICAP_F")
    private Integer effectifAlpha2549HandicapF;

    @Column(name = "EFFECTIF_ALPHA_50_PLUS_F")
    private Integer effectifAlpha50PlusF;

    @Column(name = "EFFECTIF_ALPHA_50_PLUS_H")
    private Integer effectifAlpha50PlusH;

    @Column(name = "EFFECTIF_ALPHA_50_PLUS_IVOIRIEN_H")
    private Integer effectifAlpha50PlusIvoirienH;

    @Column(name = "EFFECTIF_ALPHA_50_PLUS_IVOIRIEN_F")
    private Integer effectifAlpha50PlusIvoirienF;

    @Column(name = "EFFECTIF_ALPHA_50_PLUS_HANDICAP_H")
    private Integer effectifAlpha50PlusHandicapH;

    @Column(name = "EFFECTIF_ALPHA_50_PLUS_HANDICAP_F")
    private Integer effectifAlpha50PlusHandicapF;


}