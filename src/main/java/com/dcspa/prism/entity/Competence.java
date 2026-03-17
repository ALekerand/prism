package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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

    @Size(max = 10)
    @Column(name = "CODE_COMPETENCE", length = 10)
    private String codeCompetence;

    @Size(max = 20)
    @Column(name = "LIBELLE_COMPETENCE", length = 20)
    private String libelleCompetence;


}