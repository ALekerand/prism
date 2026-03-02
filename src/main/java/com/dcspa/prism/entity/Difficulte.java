package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "difficulte")
public class Difficulte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DIFFICULTE", nullable = false)
    private Integer id;

    @Column(name = "CODE_DIFFICULTE", length = 10)
    private String codeDifficulte;

    @Column(name = "LIBELLE_DIFFICULTE", length = 50)
    private String libelleDifficulte;


}