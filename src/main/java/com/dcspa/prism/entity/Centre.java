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
@Table(name = "centre")
@AutoCode(field = "codeCentre")
@EntityListeners(AutoCodeEntityListener.class)
public class Centre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CENTRE", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_LOCALITE", nullable = false)
    private LocaliteDImplantation idLocalite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERIODICITE")
    private Periodicite idPeriodicite;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_IEP", nullable = false)
    private Iep idIep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AUTORITE_AUTORISATION")
    private AutoriteAutorisation idAutoriteAutorisation;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NATURECENTRE", nullable = false)
    private Naturecentre idNaturecentre;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROMOTEUR", nullable = false)
    private Promoteur idPromoteur;

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

    @Column(name = "TOTAL_APPRENANTS")
    private Integer totalApprenants;

    @Column(name = "TOTAL_HOMMES")
    private Integer totalHommes;

    @Column(name = "TOTAL_FEMMES")
    private Integer totalFemmes;

    @Size(max = 50)
    @Column(name = "LATITUDE_GPS", length = 50)
    private String latitudeGps;

    @Size(max = 50)
    @Column(name = "LONGITUDE_GPS", length = 50)
    private String longitudeGps;

    @Column(name = "GPS_VALIDE")
    private Boolean gpsValide;

    @Size(max = 150)
    @Column(name = "STRUCTURE_PARTENAIRE", length = 150)
    private String structurePartenaire;

    @Size(max = 150)
    @Column(name = "NOM_PARTENAIRE", length = 150)
    private String nomPartenaire;

    @Size(max = 100)
    @Column(name = "LOCALISATION_CENTRE", length = 100)
    private String localisationCentre;

    @Size(max = 100)
    @Column(name = "NOM_MILIEU_IMPLENTATION", length = 100)
    private String nomMilieuImplentation;


}