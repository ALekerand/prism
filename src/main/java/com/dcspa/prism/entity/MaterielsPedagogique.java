package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "materiels_pedagogique")
public class MaterielsPedagogique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATERIEL_PEDAGOGIQUE", nullable = false)
    private Integer id;

    @Column(name = "CODE_MATERIEL_PEDAGOGIQUE", length = 10)
    private String codeMaterielPedagogique;

    @Column(name = "LIBELLE_MATERIEL_PEDAGOGIQUE", length = 50)
    private String libelleMaterielPedagogique;


}