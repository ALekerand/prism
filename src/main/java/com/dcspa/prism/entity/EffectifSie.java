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
@Table(name = "effectif_sie")
@AutoCode(field = "codeEffectifSie")
@EntityListeners(AutoCodeEntityListener.class)
public class EffectifSie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT20", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERIODE_ACTIVITE")
    private PeriodeActivite idPeriodeActivite;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_SIE", nullable = false)
    private NiveauSieCec idNiveauSie;

    @Size(max = 10)
    @Column(name = "CODE_EFFECTIF_SIE", length = 10)
    private String codeEffectifSie;

    @Column(name = "EFFECTIF_SIE_3_IVOIRIEN_H")
    private Integer effectifSie3IvoirienH;

    @Column(name = "EFFECTIF_SIE_3_IVOIRIEN_F")
    private Integer effectifSie3IvoirienF;

    @Column(name = "EFFECTIF_SIE_3_HANDICAP_H")
    private Integer effectifSie3HandicapH;

    @Column(name = "EFFECTIF_SIE_3_HANDICAP_F")
    private Integer effectifSie3HandicapF;

    @Column(name = "EFFECTIF_SIE_3_NON_IVOIRIEN_F")
    private Integer effectifSie3NonIvoirienF;

    @Column(name = "EFFECTIF_SIE_3_NON_IVOIRIEN_H")
    private Integer effectifSie3NonIvoirienH;

    @Column(name = "EFFECTIF_SIE_4_6_IVOIRIEN_F")
    private Integer effectifSie46IvoirienF;

    @Column(name = "EFFECTIF_SIE_4_6_IVOIRIEN_H")
    private Integer effectifSie46IvoirienH;

    @Column(name = "EFFECTIF_SIE_4_6_HANDICAP_H")
    private Integer effectifSie46HandicapH;

    @Column(name = "EFFECTIF_SIE_4_6_HANDICAP_F")
    private Integer effectifSie46HandicapF;

    @Column(name = "EFFECTIF_SIE_4_6_NON_IVOIRIIEN_H")
    private Integer effectifSie46NonIvoiriienH;

    @Column(name = "EFFECTIF_SIE_7_9_IVOIRIEN_H")
    private Integer effectifSie79IvoirienH;

    @Column(name = "EFFECTIF_SIE_7_9_IVOIRIEN_F")
    private Integer effectifSie79IvoirienF;

    @Column(name = "EFFECTIF_SIE_7_9_HANDICAP_H")
    private Integer effectifSie79HandicapH;

    @Column(name = "EFFECTIF_SIE_7_9_HANDICAP_F")
    private Integer effectifSie79HandicapF;

    @Column(name = "EFFECTIF_SIE_7_9_NON_IVOIRIEN_F")
    private Integer effectifSie79NonIvoirienF;

    @Column(name = "EFFECTIF_SIE_7_9_NON_IVOIRIEN_H")
    private Integer effectifSie79NonIvoirienH;

    @Column(name = "EFFECTIF_SIE_10_12_IVOIRIEN_F")
    private Integer effectifSie1012IvoirienF;

    @Column(name = "EFFECTIF_SIE_10_12_IVOIRIEN_H")
    private Integer effectifSie1012IvoirienH;

    @Column(name = "EFFECTIF_SIE_10_12_HANDICAP_H")
    private Integer effectifSie1012HandicapH;

    @Column(name = "EFFECTIF_SIE_10_12_HANDICAP_F")
    private Integer effectifSie1012HandicapF;

    @Column(name = "EFFECTIF_SIE_10_12_NON_IVOIRIEN_H")
    private Integer effectifSie1012NonIvoirienH;

    @Column(name = "EFFECTIF_SIE_10_12_NON_IVOIRIEN_F")
    private Integer effectifSie1012NonIvoirienF;

    @Column(name = "EFFECTIF_SIE_13_14_ET_PLUS_IVOIRIEN_F")
    private Integer effectifSie1314EtPlusIvoirienF;

    @Column(name = "EFFECTIF_SIE_13_14_ET_PLUS_IVOIRIEN_H")
    private Integer effectifSie1314EtPlusIvoirienH;

    @Column(name = "EFFECTIF_SIE_13_14_ET_PLUS_HANDICAP_F")
    private Integer effectifSie1314EtPlusHandicapF;

    @Column(name = "EFFECTIF_SIE_13_14_ET_PLUS_HANDICAP_H")
    private Integer effectifSie1314EtPlusHandicapH;

    @Column(name = "EFFECTIF_SIE_NIVEAU_SIE")
    private Integer effectifSieNiveauSie;
    @Column(name = "EFFECTIF_SIE_NIVEAU_H")
    private Integer effectifSieNiveauH;

    @Column(name = "EFFECTIF_SIE_NIVEAU_F")
    private Integer effectifSieNiveauF;


    @Column(name = "EFFECTIF_SIE_4_6_NON_IVOIRIIEN_F")
    private Integer effectifSie46NonIvoiriienF;


}