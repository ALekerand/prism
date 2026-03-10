package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "effectif_admis_integration_cp")
public class EffectifAdmisIntegrationCp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EFFECTIF_DEBUT16", nullable = false)
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

    @Column(name = "CODE_EFFECTIF_ADMIS_INTEGRATION_CP", length = 10)
    private String codeEffectifAdmisIntegrationCp;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_9_11_IVOIRIEN_H")
    private Integer effectifAdmisIntegrationCp911IvoirienH;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_9_11_IVOIRIEN_F")
    private Integer effectifAdmisIntegrationCp911IvoirienF;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_9_11_HANDICAP_H")
    private Integer effectifAdmisIntegrationCp911HandicapH;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_9_11_HANDICAP_F")
    private Integer effectifAdmisIntegrationCp911HandicapF;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_9_11_NON_IVOIRIEN_F")
    private Integer effectifAdmisIntegrationCp911NonIvoirienF;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_9_11_NON_IVOIRIEN_H")
    private Integer effectifAdmisIntegrationCp911NonIvoirienH;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_12_13_IVOIRIEN_F")
    private Integer effectifAdmisIntegrationCp1213IvoirienF;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_12_13_IVOIRIEN_H")
    private Integer effectifAdmisIntegrationCp1213IvoirienH;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_12_13_HANDICAP_H")
    private Integer effectifAdmisIntegrationCp1213HandicapH;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_12_13_HANDICAP_F")
    private Integer effectifAdmisIntegrationCp1213HandicapF;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_12_13_NON_IVOIRIIEN_H")
    private Integer effectifAdmisIntegrationCp1213NonIvoiriienH;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_14_IVOIRIEN_H")
    private Integer effectifAdmisIntegrationCp14IvoirienH;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_14_IVOIRIEN_F")
    private Integer effectifAdmisIntegrationCp14IvoirienF;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_14_HANDICAP_H")
    private Integer effectifAdmisIntegrationCp14HandicapH;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_14_HANDICAP_F")
    private Integer effectifAdmisIntegrationCp14HandicapF;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_14_NON_IVOIRIEN_F")
    private Integer effectifAdmisIntegrationCp14NonIvoirienF;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_14_NON_IVOIRIEN_H")
    private Integer effectifAdmisIntegrationCp14NonIvoirienH;

    @Column(name = "EFFECTIF_ADMIS_INTEGRATION_CP_NIVEAU_CP")
    private Integer effectifAdmisIntegrationCpNiveauCp;


}