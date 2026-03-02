package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "campagne")
public class Campagne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COMPAGNE", nullable = false)
    private Integer id;

    @Column(name = "CODE_CAMPAGNE", length = 10)
    private String codeCampagne;

    @Column(name = "DATE_DEBUT_CAMPAGNE")
    private LocalDate dateDebutCampagne;

    @Column(name = "DATE_FIN_CAMPAGNE")
    private LocalDate dateFinCampagne;

    @Column(name = "ETAT_CAMPAGNE")
    private Boolean etatCampagne;


}