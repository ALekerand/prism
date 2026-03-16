package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fonctionnalite")
public class Fonctionnalite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FONCTIONNALITE", nullable = false)
    private Integer id;

    @Column(name = "CODE_FONCTIONNALITE", nullable = false, unique = true, length = 50)
    private String codeFonctionnalite;

    @Column(name = "LIBELLE_FONCTIONNALITE", length = 100)
    private String libelleFonctionnalite;

    @Column(name = "MODULE", length = 50)
    private String module;
}
