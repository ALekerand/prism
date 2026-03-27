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
@Table(name = "effectif_reverse_formel_sie")
@AutoCode(field = "codeEffectifReverseFormelSie")
@EntityListeners(AutoCodeEntityListener.class)
public class EffectifReverseFormelSie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT23", nullable = false)
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
    @Column(name = "CODE_EFFECTIF_REVERSE_FORMEL_SIE", length = 10)
    private String codeEffectifReverseFormelSie;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_3_IVOIRIEN_H")
    private Integer effectifReverseFormelSie3IvoirienH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_3_IVOIRIEN_F")
    private Integer effectifReverseFormelSie3IvoirienF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_3_HANDICAP_H")
    private Integer effectifReverseFormelSie3HandicapH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_3_HANDICAP_F")
    private Integer effectifReverseFormelSie3HandicapF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_3_NON_IVOIRIEN_F")
    private Integer effectifReverseFormelSie3NonIvoirienF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_3_NON_IVOIRIEN_H")
    private Integer effectifReverseFormelSie3NonIvoirienH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_4_6_IVOIRIEN_F")
    private Integer effectifReverseFormelSie46IvoirienF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_4_6_IVOIRIEN_H")
    private Integer effectifReverseFormelSie46IvoirienH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_4_6_HANDICAP_H")
    private Integer effectifReverseFormelSie46HandicapH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_4_6_HANDICAP_F")
    private Integer effectifReverseFormelSie46HandicapF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_4_6_NON_IVOIRIIEN_H")
    private Integer effectifReverseFormelSie46NonIvoiriienH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_4_6_NON_IVOIRIIEN_F")
    private Integer effectifReverseFormelSie46NonIvoiriienF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_7_9_IVOIRIEN_H")
    private Integer effectifReverseFormelSie79IvoirienH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_7_9_IVOIRIEN_F")
    private Integer effectifReverseFormelSie79IvoirienF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_7_9_HANDICAP_H")
    private Integer effectifReverseFormelSie79HandicapH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_7_9_HANDICAP_F")
    private Integer effectifReverseFormelSie79HandicapF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_7_9_NON_IVOIRIEN_F")
    private Integer effectifReverseFormelSie79NonIvoirienF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_7_9_NON_IVOIRIEN_H")
    private Integer effectifReverseFormelSie79NonIvoirienH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_10_12_IVOIRIEN_F")
    private Integer effectifReverseFormelSie1012IvoirienF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_10_12_IVOIRIEN_H")
    private Integer effectifReverseFormelSie1012IvoirienH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_10_12_HANDICAP_H")
    private Integer effectifReverseFormelSie1012HandicapH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_10_12_HANDICAP_F")
    private Integer effectifReverseFormelSie1012HandicapF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_10_12_NON_IVOIRIEN_H")
    private Integer effectifReverseFormelSie1012NonIvoirienH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_10_12_NON_IVOIRIEN_F")
    private Integer effectifReverseFormelSie1012NonIvoirienF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_13_14_ET_PLUS_IVOIRIEN_F")
    private Integer effectifReverseFormelSie1314EtPlusIvoirienF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_13_14_ET_PLUS_IVOIRIEN_H")
    private Integer effectifReverseFormelSie1314EtPlusIvoirienH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_13_14_ET_PLUS_HANDICAP_F")
    private Integer effectifReverseFormelSie1314EtPlusHandicapF;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_13_14_ET_PLUS_HANDICAP_H")
    private Integer effectifReverseFormelSie1314EtPlusHandicapH;

    @Column(name = "EFFECTIF_REVERSE_FORMEL_SIE_NIVEAU_SIE")
    private Integer effectifReverseFormelSieNiveauSie;


}