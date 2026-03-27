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
@Table(name = "effectif_abandon_cp")
@AutoCode(field = "codeEffectifAbandonCp")
@EntityListeners(AutoCodeEntityListener.class)
public class EffectifAbandonCp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT15", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_CP", nullable = false)
    private NiveauCp idNiveauCp;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Cp idCentre;

    @Size(max = 10)
    @Column(name = "CODE_EFFECTIF_ABANDON_CP", length = 10)
    private String codeEffectifAbandonCp;

    @Column(name = "EFFECTIF_ABANDON_CP_9_11_IVOIRIEN_H")
    private Integer effectifAbandonCp911IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_CP_9_11_IVOIRIEN_F")
    private Integer effectifAbandonCp911IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_CP_9_11_HANDICAP_H")
    private Integer effectifAbandonCp911HandicapH;

    @Column(name = "EFFECTIF_ABANDON_CP_9_11_HANDICAP_F")
    private Integer effectifAbandonCp911HandicapF;

    @Column(name = "EFFECTIF_ABANDON_CP_9_11_NON_IVOIRIEN_F")
    private Integer effectifAbandonCp911NonIvoirienF;

    @Column(name = "EFFECTIF_ABANDON_CP_9_11_NON_IVOIRIEN_H")
    private Integer effectifAbandonCp911NonIvoirienH;

    @Column(name = "EFFECTIF_ABANDON_CP_12_13_IVOIRIEN_F")
    private Integer effectifAbandonCp1213IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_CP_12_13_IVOIRIEN_H")
    private Integer effectifAbandonCp1213IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_CP_12_13_HANDICAP_H")
    private Integer effectifAbandonCp1213HandicapH;

    @Column(name = "EFFECTIF_ABANDON_CP_12_13_HANDICAP_F")
    private Integer effectifAbandonCp1213HandicapF;

    @Column(name = "EFFECTIF_ABANDON_CP_12_13_NON_IVOIRIIEN_H")
    private Integer effectifAbandonCp1213NonIvoiriienH;

    @Column(name = "EFFECTIF_ABANDON_CP_14_IVOIRIEN_H")
    private Integer effectifAbandonCp14IvoirienH;

    @Column(name = "EFFECTIF_ABANDON_CP_14_IVOIRIEN_F")
    private Integer effectifAbandonCp14IvoirienF;

    @Column(name = "EFFECTIF_ABANDON_CP_14_HANDICAP_H")
    private Integer effectifAbandonCp14HandicapH;

    @Column(name = "EFFECTIF_ABANDON_CP_14_HANDICAP_F")
    private Integer effectifAbandonCp14HandicapF;

    @Column(name = "EFFECTIF_ABANDON_CP_14_NON_IVOIRIEN_F")
    private Integer effectifAbandonCp14NonIvoirienF;

    @Column(name = "EFFECTIF_ABANDON_CP_14_NON_IVOIRIEN_H")
    private Integer effectifAbandonCp14NonIvoirienH;

    @Column(name = "EFFECTIF_ABANDON_CP_NIVEAU_CP")
    private Integer effectifAbandonCpNiveauCp;

    @Lob
    @Column(name = "CAUSE_ABANDON_CP")
    private String causeAbandonCp;


}