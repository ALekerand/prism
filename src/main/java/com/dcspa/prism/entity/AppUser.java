package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USER", nullable = false)
    private Integer id;

    @Column(name = "USERNAME", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "PASSWORD_HASH", nullable = false, length = 255)
    @JsonIgnore
    private String passwordHash;

    @Column(name = "EMAIL", length = 150)
    private String email;

    @Column(name = "ACTIF")
    private Boolean actif;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_REGION")
    private Region idRegion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DRENA")
    private Drena idDrena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_IEP")
    private Iep idIep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DEPARTEMENT")
    private Departement idDepartement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SOUS_PREFECTURE")
    private SousPrefecture idSousPrefecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMMUNE")
    private Commune idCommune;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LOCALITE")
    private LocaliteDImplantation idLocalite;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "ID_USER"),
            inverseJoinColumns = @JoinColumn(name = "ID_ROLE")
    )
    @JsonIgnore
    private Set<AppRole> roles = new HashSet<>();
}
