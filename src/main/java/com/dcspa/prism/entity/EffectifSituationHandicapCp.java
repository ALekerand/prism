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
@Table(name = "effectif_situation_handicap_cp")
@AutoCode(field = "codeEffectifSituationHandicapCp")
@EntityListeners(AutoCodeEntityListener.class)
public class EffectifSituationHandicapCp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT19", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_CP", nullable = false)
    private NiveauCp idNiveauCp;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Cp idCentre;

    @Size(max = 10)
    @Column(name = "CODE_EFFECTIF_SITUATION_HANDICAP_CP", length = 10)
    private String codeEffectifSituationHandicapCp;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_9_11_IVOIRIEN_H")
    private Integer effectifSituationHandicapCp911IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_9_11_IVOIRIEN_F")
    private Integer effectifSituationHandicapCp911IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_9_11_HANDICAP_H")
    private Integer effectifSituationHandicapCp911HandicapH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_9_11_HANDICAP_F")
    private Integer effectifSituationHandicapCp911HandicapF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_9_11_NON_IVOIRIEN_F")
    private Integer effectifSituationHandicapCp911NonIvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_9_11_NON_IVOIRIEN_H")
    private Integer effectifSituationHandicapCp911NonIvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_12_13_IVOIRIEN_F")
    private Integer effectifSituationHandicapCp1213IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_12_13_IVOIRIEN_H")
    private Integer effectifSituationHandicapCp1213IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_12_13_HANDICAP_H")
    private Integer effectifSituationHandicapCp1213HandicapH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_12_13_HANDICAP_F")
    private Integer effectifSituationHandicapCp1213HandicapF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_12_13_NON_IVOIRIIEN_H")
    private Integer effectifSituationHandicapCp1213NonIvoiriienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_14_IVOIRIEN_H")
    private Integer effectifSituationHandicapCp14IvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_14_IVOIRIEN_F")
    private Integer effectifSituationHandicapCp14IvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_14_HANDICAP_H")
    private Integer effectifSituationHandicapCp14HandicapH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_14_HANDICAP_F")
    private Integer effectifSituationHandicapCp14HandicapF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_14_NON_IVOIRIEN_F")
    private Integer effectifSituationHandicapCp14NonIvoirienF;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_14_NON_IVOIRIEN_H")
    private Integer effectifSituationHandicapCp14NonIvoirienH;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_NIVEAU_CP")
    private Integer effectifSituationHandicapCpNiveauCp;

    @Column(name = "EFFECTIF_SITUATION_HANDICAP_CP_12_13_NON_IVOIRIIEN_F")
    private Integer effectifSituationHandicapCp1213NonIvoiriienF;


}