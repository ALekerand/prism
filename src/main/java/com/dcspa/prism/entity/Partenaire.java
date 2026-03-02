package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "partenaire")
public class Partenaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PARTENAIRE", nullable = false)
    private Integer id;

    @Column(name = "CODE_PARTENAIRE", length = 50)
    private String codePartenaire;

    @Column(name = "LIBELLE_PARTENAIRE", length = 100)
    private String libellePartenaire;


}