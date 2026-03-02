package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "centre")
public class Centre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CENTRE", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERIODICITE")
    private Periodicite idPeriodicite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AUTORITE_AUTORISATION")
    private AutoriteAutorisation idAutoriteAutorisation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROMOTEUR", nullable = false)
    private Promoteur idPromoteur;

    @Column(name = "CODE_CENTRE", length = 50)
    private String codeCentre;

    @Column(name = "AUTORISATION")
    private Boolean autorisation;

    @Column(name = "ENCADREUR_NON__MENA", length = 150)
    private String encadreurNonMena;

    @Column(name = "ENCADRER_PAR_MENA")
    private Boolean encadrerParMena;

    @Column(name = "EST_ELECTRIFIE")
    private Boolean estElectrifie;

    @Column(name = "A_DE_LEAU")
    private Boolean aDeLeau;

    @Column(name = "NOMBRE_VISITE")
    private Integer nombreVisite;


}