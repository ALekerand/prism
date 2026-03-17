package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_abandon_cec")
public class EffectifAbandonCec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT10", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_SIE", nullable = false)
    private NiveauSieCec idNiveauSie;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Cec idCentre;

    @Size(max = 10)
    @Column(name = "CODE_EFFECTIF_ABANDON_CEC", length = 10)
    private String codeEffectifAbandonCec;

    @Column(name = "EFFECTIF_ABANDON_CEC_MOINS_3_F")
    private Integer effectifAbandonCecMoins3F;

    @Column(name = "EFFECTIF_ABANDON_CEC_MOINS_3_H")
    private Integer effectifAbandonCecMoins3H;

    @Column(name = "EFFECTIF_ABANDON_CEC_MOINS_3_IVOIRIEN_H")
    private Integer effectifAbandonCecMoins3IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_CEC_MOINS_3_IVOIRIEN_F")
    private Integer effectifAbandonCecMoins3IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_CEC_MOINS_3_HANDICAP_H")
    private Integer effectifAbandonCecMoins3HandicapH;

    @Column(name = "EFFECTIF_ABANDON_CEC_MOINS_3_HANDICAP_F")
    private Integer effectifAbandonCecMoins3HandicapF;

    @Column(name = "EFFECTIF_ABANDON_CEC_3_5_F")
    private Integer effectifAbandonCec35F;

    @Column(name = "EFFECTIF_ABANDON_CEC_3_5_H")
    private Integer effectifAbandonCec35H;

    @Column(name = "EFFECTIF_ABANDON_CEC_3_5_IVOIRIEN_H")
    private Integer effectifAbandonCec35IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_CEC_3_5_IVOIRIEN_F")
    private Integer effectifAbandonCec35IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_CEC_3_5_HANDICAP_H")
    private Integer effectifAbandonCec35HandicapH;

    @Column(name = "EFFECTIF_ABANDON_CEC_3_5_HANDICAP_F")
    private Integer effectifAbandonCec35HandicapF;

    @Column(name = "EFFECTIF_ABANDON_CEC_6_8_F")
    private Integer effectifAbandonCec68F;

    @Column(name = "EFFECTIF_ABANDON_CEC_6_8_H")
    private Integer effectifAbandonCec68H;

    @Column(name = "EFFECTIF_ABANDON_CEC_6_8_IVOIRIEN_F")
    private Integer effectifAbandonCec68IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_CEC_6_8_IVOIRIEN_H")
    private Integer effectifAbandonCec68IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_CEC_6_8_HANDICAP_H")
    private Integer effectifAbandonCec68HandicapH;

    @Column(name = "EFFECTIF_ABANDON_CEC_6_8_HANDICAP_F")
    private Integer effectifAbandonCec68HandicapF;

    @Column(name = "EFFECTIF_ABANDON_CEC_9_11_H")
    private Integer effectifAbandonCec911H;

    @Column(name = "EFFECTIF_ABANDON_CEC_9_11_F")
    private Integer effectifAbandonCec911F;

    @Column(name = "EFFECTIF_ABANDON_CEC_9_11_IVOIRIEN_H")
    private Integer effectifAbandonCec911IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_CEC_9_11_IVOIRIEN_F")
    private Integer effectifAbandonCec911IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_CEC_9_11_HANDICAP_H")
    private Integer effectifAbandonCec911HandicapH;

    @Column(name = "EFFECTIF_ABANDON_CEC_9_11_HANDICAP_F")
    private Integer effectifAbandonCec911HandicapF;

    @Column(name = "EFFECTIF_ABANDON_CEC_12_16_F")
    private Integer effectifAbandonCec1216F;

    @Column(name = "EFFECTIF_ABANDON_CEC_12_16_H")
    private Integer effectifAbandonCec1216H;

    @Column(name = "EFFECTIF_ABANDON_CEC_12_16_IVOIRIEN_H")
    private Integer effectifAbandonCec1216IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_CEC_12_16_IVOIRIEN_F")
    private Integer effectifAbandonCec1216IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_CEC_12_16_HANDICAP_H")
    private Integer effectifAbandonCec1216HandicapH;

    @Column(name = "EFFECTIF_ABANDON_CEC_12_16_HANDICAP_F")
    private Integer effectifAbandonCec1216HandicapF;

    @Column(name = "EFFECTIF_ABANDON_CEC_NIVEAU_CEC")
    private Integer effectifAbandonCecNiveauCec;

    @Lob
    @Column(name = "CAUSE_ABANDON_CEC")
    private String causeAbandonCec;


}