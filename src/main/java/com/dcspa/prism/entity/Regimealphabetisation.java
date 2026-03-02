package com.dcspa.prism.entity;

import jakarta.persistence.*;
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

    //TODO [Reverse Engineering] generate columns from DB
}