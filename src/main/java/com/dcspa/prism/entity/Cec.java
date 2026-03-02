package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cec")
public class Cec {
    @Id
    @Column(name = "ID_CENTRE", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Centre centre;

    @Column(name = "ID_PERIODICITE")
    private Integer idPeriodicite;

    @Column(name = "ID_AUTORITE_AUTORISATION")
    private Integer idAutoriteAutorisation;

    @Column(name = "ID_PROMOTEUR")
    private Integer idPromoteur;

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

    @Column(name = "LIBELLE_CEC", length = 100)
    private String libelleCec;


}