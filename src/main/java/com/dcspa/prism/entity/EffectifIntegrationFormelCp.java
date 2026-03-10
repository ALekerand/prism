package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_integration_formel_cp")
public class EffectifIntegrationFormelCp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT17", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_CP", nullable = false)
    private NiveauCp idNiveauCp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Cp idCentre;

    @Column(name = "CODE_EFFECTIF_INTEGRATION_FORMEL_CP", length = 10)
    private String codeEffectifIntegrationFormelCp;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_9_11_IVOIRIEN_H")
    private Integer effectifIntegrationFormelCp911IvoirienH;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_9_11_IVOIRIEN_F")
    private Integer effectifIntegrationFormelCp911IvoirienF;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_9_11_HANDICAP_H")
    private Integer effectifIntegrationFormelCp911HandicapH;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_9_11_HANDICAP_F")
    private Integer effectifIntegrationFormelCp911HandicapF;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_9_11_NON_IVOIRIEN_F")
    private Integer effectifIntegrationFormelCp911NonIvoirienF;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_9_11_NON_IVOIRIEN_H")
    private Integer effectifIntegrationFormelCp911NonIvoirienH;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_12_13_IVOIRIEN_F")
    private Integer effectifIntegrationFormelCp1213IvoirienF;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_12_13_IVOIRIEN_H")
    private Integer effectifIntegrationFormelCp1213IvoirienH;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_12_13_HANDICAP_H")
    private Integer effectifIntegrationFormelCp1213HandicapH;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_12_13_HANDICAP_F")
    private Integer effectifIntegrationFormelCp1213HandicapF;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_12_13_NON_IVOIRIIEN_H")
    private Integer effectifIntegrationFormelCp1213NonIvoiriienH;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_14_IVOIRIEN_H")
    private Integer effectifIntegrationFormelCp14IvoirienH;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_14_IVOIRIEN_F")
    private Integer effectifIntegrationFormelCp14IvoirienF;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_14_HANDICAP_H")
    private Integer effectifIntegrationFormelCp14HandicapH;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_14_HANDICAP_F")
    private Integer effectifIntegrationFormelCp14HandicapF;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_14_NON_IVOIRIEN_F")
    private Integer effectifIntegrationFormelCp14NonIvoirienF;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_14_NON_IVOIRIEN_H")
    private Integer effectifIntegrationFormelCp14NonIvoirienH;

    @Column(name = "EFFECTIF_INTEGRATION_FORMEL_CP_NIVEAU_CP")
    private Integer effectifIntegrationFormelCpNiveauCp;


}