package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_cepe_cec")
public class EffectifCepeCec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT12", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Cec idCentre;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CEC_ID_CENTRE", nullable = false)
    private Cec cecIdCentre;

    @Size(max = 10)
    @Column(name = "CODE_EFFECTIF_CEPE_CEC", length = 10)
    private String codeEffectifCepeCec;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_FILLE_CEC")
    private Integer effectifCepeCandidatFilleCec;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_GARCON_CEC")
    private Integer effectifCepeCandidatGarconCec;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_IVOIRIEN_CEC")
    private Integer effectifCepeCandidatIvoirienCec;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_HANDICAP_FILLE_CEC")
    private Integer effectifCepeCandidatHandicapFilleCec;

    @Column(name = "EFFECTIF_CEPE_CANDIDAT_HANDICAP_GARCON_CEC")
    private Integer effectifCepeCandidatHandicapGarconCec;

    @Column(name = "EFFECTIF_CEPE_ADMIS_FILLE_CEC")
    private Integer effectifCepeAdmisFilleCec;

    @Column(name = "EFFECTIF_CEPE_ADMIS_GARCON_CEC")
    private Integer effectifCepeAdmisGarconCec;

    @Column(name = "EFFECTIF_CEPE_ADMIS_IVOIRIEN_CEC")
    private Integer effectifCepeAdmisIvoirienCec;

    @Column(name = "EFFECTIF_CEPE_ADMIS_HANDICAP_FILLE_CEC")
    private Integer effectifCepeAdmisHandicapFilleCec;

    @Column(name = "EFFECTIF_CEPE_ADMIS_HANDICAP_GARCON_CEC")
    private Integer effectifCepeAdmisHandicapGarconCec;


}