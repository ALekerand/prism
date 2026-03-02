package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "ressource_financiere_materiel")
public class RessourceFinanciereMateriel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESSOURCE_FINANCIERE", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Centre idCentre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_DESIGNATION", nullable = false)
    private Designation idDesignation;

    @Column(name = "SOURCE_FINANCEMENT", length = 50)
    private String sourceFinancement;

    @Column(name = "MONTANT", precision = 12)
    private BigDecimal montant;


}