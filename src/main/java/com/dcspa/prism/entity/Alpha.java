package com.dcspa.prism.entity;

import com.dcspa.prism.codegen.AutoCode;
import com.dcspa.prism.codegen.AutoCodeEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "alpha")
@AutoCode(field = "codeAlpha")
@EntityListeners(AutoCodeEntityListener.class)
public class Alpha {
    @Id
    @Column(name = "ID_CENTRE", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    @JsonIgnore
    private Centre centre;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_COMPAGNE", nullable = false)
    private Campagne idCompagne;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CATEGORIE_CENTRE_ALPHA", nullable = false)
    private CategorieCentreAlpha idCategorieCentreAlpha;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_TYPE_ALPHA", nullable = false)
    private TypeAlpha idTypeAlpha;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_REGIME_ALPHA", nullable = false)
    private Regimealphabetisation idRegimeAlpha;

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

    @Size(max = 50)
    @Column(name = "CODE_ALPHA", length = 50)
    private String codeAlpha;

    @Size(max = 100)
    @Column(name = "LIBELLE_ALPHA", length = 100)
    private String libelleAlpha;


}