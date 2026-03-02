package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "anne_scolaire")
public class AnneScolaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ANNEE_SCOLAIRE", nullable = false)
    private Integer id;

    @Column(name = "CODE_ANNEE_SCOLAIRE", length = 10)
    private String codeAnneeScolaire;

    @Column(name = "DEBUT_ANNEE_SCOLAIRE")
    private LocalDate debutAnneeScolaire;

    @Column(name = "FIN_ANNEE_SCOLAIRE")
    private LocalDate finAnneeScolaire;

    @Column(name = "ETAT_ANNEE_SCOLAIRE")
    private Boolean etatAnneeScolaire;


}