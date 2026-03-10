package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cec_niveau")
public class CecNiveau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CC_NIVEAU", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NIVEAU_SIE", nullable = false)
    private NiveauSieCec idNiveauSie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private AnneScolaire idAnneeScolaire;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Cec idCentre;

    @Column(name = "CODE_NIVEAU_CEC", length = 50)
    private String codeNiveauCec;

    @Column(name = "NOMBRE_SALLE_CEC")
    private Integer nombreSalleCec;


}