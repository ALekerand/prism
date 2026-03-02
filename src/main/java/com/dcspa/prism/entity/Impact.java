package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "impact")
public class Impact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_IMPACT", nullable = false)
    private Integer id;

    @Column(name = "CODE_IMPACT", length = 10)
    private String codeImpact;

    @Column(name = "LIBELLE_IMPACT", length = 50)
    private String libelleImpact;


}