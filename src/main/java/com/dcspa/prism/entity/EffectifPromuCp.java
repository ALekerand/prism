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
@Table(name = "effectif_promu_cp")
@AutoCode(field = "codeEffectifPromuCp")
@EntityListeners(AutoCodeEntityListener.class)
public class EffectifPromuCp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_PROMU_CP", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_CP", nullable = false)
    private NiveauCp idNiveauCp;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERIODE_ACTIVITE")
    private PeriodeActivite idPeriodeActivite;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Cp idCentre;

    @Size(max = 10)
    @Column(name = "CODE_EFFECTIF_PROMU_CP", length = 10)
    private String codeEffectifPromuCp;

    @Column(name = "EFFECTIF_PROMU_CP_9_11_IVOIRIEN_H")
    private Integer effectifPromuCp911IvoirienH;

    @Column(name = "EFFECTIF_PROMU_CP_9_11_IVOIRIEN_F")
    private Integer effectifPromuCp911IvoirienF;

    @Column(name = "EFFECTIF_PROMU_CP_9_11_HANDICAP_H")
    private Integer effectifPromuCp911HandicapH;

    @Column(name = "EFFECTIF_PROMU_CP_9_11_HANDICAP_F")
    private Integer effectifPromuCp911HandicapF;

    @Column(name = "EFFECTIF_PROMU_CP_9_11_NON_IVOIRIEN_F")
    private Integer effectifPromuCp911NonIvoirienF;

    @Column(name = "EFFECTIF_PROMU_CP_9_11_NON_IVOIRIEN_H")
    private Integer effectifPromuCp911NonIvoirienH;

    @Column(name = "EFFECTIF_PROMU_CP_12_13_IVOIRIEN_F")
    private Integer effectifPromuCp1213IvoirienF;

    @Column(name = "EFFECTIF_PROMU_CP_12_13_IVOIRIEN_H")
    private Integer effectifPromuCp1213IvoirienH;

    @Column(name = "EFFECTIF_PROMU_CP_12_13_HANDICAP_H")
    private Integer effectifPromuCp1213HandicapH;

    @Column(name = "EFFECTIF_PROMU_CP_12_13_HANDICAP_F")
    private Integer effectifPromuCp1213HandicapF;

    @Column(name = "EFFECTIF_PROMU_CP_12_13_NON_IVOIRIIEN_H")
    private Integer effectifPromuCp1213NonIvoiriienH;

    @Column(name = "EFFECTIF_PROMU_CP_12_13_NON_IVOIRIIEN_F")
    private Integer effectifPromuCp1213NonIvoiriienF;

    @Column(name = "EFFECTIF_PROMU_CP_14_IVOIRIEN_H")
    private Integer effectifPromuCp14IvoirienH;

    @Column(name = "EFFECTIF_PROMU_CP_14_IVOIRIEN_F")
    private Integer effectifPromuCp14IvoirienF;

    @Column(name = "EFFECTIF_PROMU_CP_14_HANDICAP_H")
    private Integer effectifPromuCp14HandicapH;

    @Column(name = "EFFECTIF_PROMU_CP_14_HANDICAP_F")
    private Integer effectifPromuCp14HandicapF;

    @Column(name = "EFFECTIF_PROMU_CP_14_NON_IVOIRIEN_F")
    private Integer effectifPromuCp14NonIvoirienF;

    @Column(name = "EFFECTIF_PROMU_CP_14_NON_IVOIRIEN_H")
    private Integer effectifPromuCp14NonIvoirienH;

    @Column(name = "EFFECTIF_PROMU_CP_NIVEAU_CP")
    private Integer effectifPromuCpNiveauCp;

    @Column(name = "EFFECTIF_PROMU_CP_NIVEAU_H")
    private Integer effectifPromuCpNiveauH;

    @Column(name = "EFFECTIF_PROMU_CP_NIVEAU_F")
    private Integer effectifPromuCpNiveauF;
}
