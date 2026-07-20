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
@Table(name = "effectif_cepe_cp")
@AutoCode(field = "codeEffectifCepeCp")
@EntityListeners(AutoCodeEntityListener.class)
public class EffectifCepeCp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT18", nullable = false)
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
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Cp idCentre;

    @Size(max = 10)
    @Column(name = "CODE_EFFECTIF_CEPE_CP", length = 10)
    private String codeEffectifCepeCp;

    @Column(name = "EFFECTIF_CEPE_CP_NIVEAU_H")
    private Integer effectifCepeCpNiveauH;

    @Column(name = "EFFECTIF_CEPE_CP_NIVEAU_F")
    private Integer effectifCepeCpNiveauF;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_F_CP")
    private Integer effectifCepeCandidatFCp;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_H_CP")
    private Integer effectifCepeCandidatHCp;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_IVOIRIEN_CP")
    private Integer effectifCepeCandidatIvoirienCp;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_HANDICAP_F_CP")
    private Integer effectifCepeCandidatHandicapFCp;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_HANDICAP_H_CP")
    private Integer effectifCepeCandidatHandicapHCp;

    @Column(name = "EFFECTIF_CEPE_ADMIS_F_CP")
    private Integer effectifCepeAdmisFCp;

    @Column(name = "EFFECTIF_CEPE_ADMIS_H_CP")
    private Integer effectifCepeAdmisHCp;

    @Column(name = "EFFECTIF_CEPE_ADMIS_IVOIRIEN_CP")
    private Integer effectifCepeAdmisIvoirienCp;

    @Column(name = "EFFECTIF_CEPE_ADMIS_HANDICAP_F_CP")
    private Integer effectifCepeAdmisHandicapFCp;

    @Column(name = "EFFECTIF_CEPE_ADMIS_HANDICAP_H_CP")
    private Integer effectifCepeAdmisHandicapHCp;


}