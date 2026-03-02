package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "diplome")
public class Diplome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DIPLOME", nullable = false)
    private Integer id;

    @Column(name = "CODE_DIPLOME", length = 50)
    private String codeDiplome;

    @Column(name = "LIBELLE_DIPLOME", length = 100)
    private String libelleDiplome;


}