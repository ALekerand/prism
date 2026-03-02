package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "competence")
public class Competence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COMPETENCE", nullable = false)
    private Integer id;

    @Column(name = "CODE_COMPETENCE", length = 10)
    private String codeCompetence;

    @Column(name = "LIBELLE_COMPETENCE", length = 20)
    private String libelleCompetence;


}