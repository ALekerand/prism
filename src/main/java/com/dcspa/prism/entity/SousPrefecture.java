package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sous_prefecture")
public class SousPrefecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SOUS_PREFECTURE", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_DEPARTEMENT", nullable = false)
    private Departement idDepartement;

    @Size(max = 10)
    @Column(name = "CODE_SOUS_PREFECTURE", length = 10)
    private String codeSousPrefecture;

    @Size(max = 30)
    @Column(name = "NOM_SOUS_PREFECTURE", length = 30)
    private String nomSousPrefecture;


}