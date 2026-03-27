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
@Table(name = "effectif_cec")
@AutoCode(field = "codeEffectifCec")
@EntityListeners(AutoCodeEntityListener.class)
public class EffectifCec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_CEC", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PERIODE_ACTIVITE", nullable = false)
    private PeriodeActivite idPeriodeActivite;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_SIE", nullable = false)
    private NiveauSieCec idNiveauSie;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Cec idCentre;

    @Size(max = 10)
    @Column(name = "CODE_EFFECTIF_CEC", length = 10)
    private String codeEffectifCec;

    @Column(name = "EFFECTIF_CEC_MOINS_3_F")
    private Integer effectifCecMoins3F;

    @Column(name = "EFFECTIF_CEC_MOINS_3_H")
    private Integer effectifCecMoins3H;

    @Column(name = "EFFECTIF_CEC_MOINS_3_IVOIRIEN_H")
    private Integer effectifCecMoins3IvoirienH;

    @Column(name = "EFFECTIF_CEC_MOINS_3_IVOIRIEN_F")
    private Integer effectifCecMoins3IvoirienF;

    @Column(name = "EFFECTIF_CEC_MOINS_3_HANDICAP_H")
    private Integer effectifCecMoins3HandicapH;

    @Column(name = "EFFECTIF_CEC_MOINS_3_HANDICAP_F")
    private Integer effectifCecMoins3HandicapF;

    @Column(name = "EFFECTIF_CEC_3_5_F")
    private Integer effectifCec35F;

    @Column(name = "EFFECTIF_CEC_3_5_H")
    private Integer effectifCec35H;

    @Column(name = "EFFECTIF_CEC_3_5_IVOIRIEN_H")
    private Integer effectifCec35IvoirienH;

    @Column(name = "EFFECTIF_CEC_3_5_IVOIRIEN_F")
    private Integer effectifCec35IvoirienF;

    @Column(name = "EFFECTIF_CEC_3_5_HANDICAP_H")
    private Integer effectifCec35HandicapH;

    @Column(name = "EFFECTIF_CEC_3_5_HANDICAP_F")
    private Integer effectifCec35HandicapF;

    @Column(name = "EFFECTIF_CEC_6_8_F")
    private Integer effectifCec68F;

    @Column(name = "EFFECTIF_CEC_6_8_H")
    private Integer effectifCec68H;

    @Column(name = "EFFECTIF_CEC_6_8_IVOIRIEN_F")
    private Integer effectifCec68IvoirienF;

    @Column(name = "EFFECTIF_CEC_6_8_IVOIRIEN_H")
    private Integer effectifCec68IvoirienH;

    @Column(name = "EFFECTIF_CEC_6_8_HANDICAP_H")
    private Integer effectifCec68HandicapH;

    @Column(name = "EFFECTIF_CEC_6_8_HANDICAP_F")
    private Integer effectifCec68HandicapF;

    @Column(name = "EFFECTIF_CEC_9_11_F")
    private Integer effectifCec911F;

    @Column(name = "EFFECTIF_CEC_9_11_H")
    private Integer effectifCec911H;

    @Column(name = "EFFECTIF_CEC_9_11_IVOIRIEN_H")
    private Integer effectifCec911IvoirienH;

    @Column(name = "EFFECTIF_CEC_9_11_IVOIRIEN_F")
    private Integer effectifCec911IvoirienF;

    @Column(name = "EFFECTIF_CEC_9_11_HANDICAP_H")
    private Integer effectifCec911HandicapH;

    @Column(name = "EFFECTIF_CEC_9_11_HANDICAP_F")
    private Integer effectifCec911HandicapF;

    @Column(name = "EFFECTIF_CEC_12_16_F")
    private Integer effectifCec1216F;

    @Column(name = "EFFECTIF_CEC_12_16_H")
    private Integer effectifCec1216H;

    @Column(name = "EFFECTIF_CEC_12_16_IVOIRIEN_H")
    private Integer effectifCec1216IvoirienH;

    @Column(name = "EFFECTIF_CEC_12_16_IVOIRIEN_F")
    private Integer effectifCec1216IvoirienF;

    @Column(name = "EFFECTIF_CEC_12_16_HANDICAP_H")
    private Integer effectifCec1216HandicapH;

    @Column(name = "EFFECTIF_CEC_12_16_HANDICAP_F")
    private Integer effectifCec1216HandicapF;

    @Column(name = "EFFECTIF_CEC_NIVEAU_CEC")
    private Integer effectifCecNiveauCec;


}