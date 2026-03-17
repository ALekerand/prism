package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "regimealphabetisation")
public class Regimealphabetisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REGIME_ALPHA", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "LIBELLE_REGIME_ALPHA", length = 100)
    private String libelleRegimeAlpha;


}