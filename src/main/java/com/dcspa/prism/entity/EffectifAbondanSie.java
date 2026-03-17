package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_abondan_sie")
public class EffectifAbondanSie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT21", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_SIE", nullable = false)
    private NiveauSieCec idNiveauSie;

    @Size(max = 10)
    @Column(name = "CODE_ABANDON_EFFECTIF_SIE", length = 10)
    private String codeAbandonEffectifSie;

    @Column(name = "EFFECTIF_ABANDON_SIE_3_IVOIRIEN_H")
    private Integer effectifAbandonSie3IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_SIE_3_IVOIRIEN_F")
    private Integer effectifAbandonSie3IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_SIE_3_HANDICAP_H")
    private Integer effectifAbandonSie3HandicapH;

    @Column(name = "EFFECTIF_ABANDON_SIE_3_HANDICAP_F")
    private Integer effectifAbandonSie3HandicapF;

    @Column(name = "EFFECTIF_ABANDON_SIE_3_NON_IVOIRIEN_F")
    private Integer effectifAbandonSie3NonIvoirienF;

    @Column(name = "EFFECTIF_ABANDON_SIE_3_NON_IVOIRIEN_H")
    private Integer effectifAbandonSie3NonIvoirienH;

    @Column(name = "EFFECTIF_ABANDON_SIE_4_6_IVOIRIEN_F")
    private Integer effectifAbandonSie46IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_SIE_4_6_IVOIRIEN_H")
    private Integer effectifAbandonSie46IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_SIE_4_6_HANDICAP_H")
    private Integer effectifAbandonSie46HandicapH;

    @Column(name = "EFFECTIF_ABANDON_SIE_4_6_HANDICAP_F")
    private Integer effectifAbandonSie46HandicapF;

    @Column(name = "EFFECTIF_ABANDON_SIE_4_6_NON_IVOIRIIEN_H")
    private Integer effectifAbandonSie46NonIvoiriienH;

    @Column(name = "EFFECTIF_ABANDON_SIE_4_6_NON_IVOIRIIEN_F")
    private Integer effectifAbandonSie46NonIvoiriienF;

    @Column(name = "EFFECTIF_ABANDON_SIE_7_9_IVOIRIEN_H")
    private Integer effectifAbandonSie79IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_SIE_7_9_IVOIRIEN_F")
    private Integer effectifAbandonSie79IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_SIE_7_9_HANDICAP_H")
    private Integer effectifAbandonSie79HandicapH;

    @Column(name = "EFFECTIF_ABANDON_SIE_7_9_HANDICAP_F")
    private Integer effectifAbandonSie79HandicapF;

    @Column(name = "EFFECTIF_ABANDON_SIE_7_9_NON_IVOIRIEN_F")
    private Integer effectifAbandonSie79NonIvoirienF;

    @Column(name = "EFFECTIF_ABANDON_SIE_7_9_NON_IVOIRIEN_H")
    private Integer effectifAbandonSie79NonIvoirienH;

    @Column(name = "EFFECTIF_ABANDON_SIE_10_12_IVOIRIEN_F")
    private Integer effectifAbandonSie1012IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_SIE_10_12_IVOIRIEN_H")
    private Integer effectifAbandonSie1012IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_SIE_10_12_HANDICAP_H")
    private Integer effectifAbandonSie1012HandicapH;

    @Column(name = "EFFECTIF_ABANDON_SIE_10_12_HANDICAP_F")
    private Integer effectifAbandonSie1012HandicapF;

    @Column(name = "EFFECTIF_ABANDON_SIE_10_12_NON_IVOIRIEN_H")
    private Integer effectifAbandonSie1012NonIvoirienH;

    @Column(name = "EFFECTIF_ABANDON_SIE_10_12_NON_IVOIRIEN_F")
    private Integer effectifAbandonSie1012NonIvoirienF;

    @Column(name = "EFFECTIF_ABANDON_SIE_13_14_ET_PLUS_IVOIRIEN_F")
    private Integer effectifAbandonSie1314EtPlusIvoirienF;

    @Column(name = "EFFECTIF_ABANDON_SIE_13_14_ET_PLUS_IVOIRIEN_H")
    private Integer effectifAbandonSie1314EtPlusIvoirienH;

    @Column(name = "EFFECTIF_ABANDON_SIE_13_14_ET_PLUS_HANDICAP_F")
    private Integer effectifAbandonSie1314EtPlusHandicapF;

    @Column(name = "EFFECTIF_ABANDON_SIE_13_14_ET_PLUS_HANDICAP_H")
    private Integer effectifAbandonSie1314EtPlusHandicapH;

    @Column(name = "EFFECTIF_ABANDON_SIE_13_14_ET_PLUS_NON_IVOIRIEN_F")
    private Integer effectifAbandonSie1314EtPlusNonIvoirienF;

    @Column(name = "EFFECTIF_ABANDON_SIE_13_14_ET_PLUS_NON_IVOIRIEN_H")
    private Integer effectifAbandonSie1314EtPlusNonIvoirienH;

    @Column(name = "EFFECTIF_ABANDON_SIE_NIVEAU_SIE")
    private Integer effectifAbandonSieNiveauSie;

    @Lob
    @Column(name = "CAUSE_ABANDON_SIE")
    private String causeAbandonSie;


}