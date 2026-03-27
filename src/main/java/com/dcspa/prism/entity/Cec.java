package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    private Centre centre;

    @Column(name = "ID_LOCALITE")
    private Integer idLocalite;

    @Column(name = "ID_PERIODICITE")
    private Integer idPeriodicite;

    @Column(name = "ID_IEP")
    private Integer idIep;

    @Column(name = "ID_AUTORITE_AUTORISATION")
    private Integer idAutoriteAutorisation;

    @Column(name = "ID_NATURECENTRE")
    private Integer idNaturecentre;

    @Column(name = "ID_PROMOTEUR")
    private Integer idPromoteur;

    @Size(max = 50)
    @Column(name = "CODE_CENTRE", length = 50)
    private String codeCentre;

    @Column(name = "AUTORISATION")
    private Boolean autorisation;

    @Size(max = 150)
    @Column(name = "ENCADREUR_NON_MENA", length = 150)
    private String encadreurNonMena;

    @Column(name = "ENCADRER_PAR_MENA")
    private Boolean encadrerParMena;

    @Column(name = "EST_ELECTRIFIE")
    private Boolean estElectrifie;

    @Column(name = "A_DE_LEAU")
    private Boolean aDeLeau;

    @Column(name = "NOMBRE_VISITE")
    private Integer nombreVisite;

    @Size(max = 100)
    @Column(name = "LOCALISATION_CENTRE", length = 100)
    private String localisationCentre;

    @Size(max = 100)
    @Column(name = "NOM_MILIEU_IMPLENTATION", length = 100)
    private String nomMilieuImplentation;

    @Size(max = 100)
    @Column(name = "LIBELLE_CEC", length = 100)
    private String libelleCec;


}