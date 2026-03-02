package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "periodicite")
public class Periodicite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERIODICITE", nullable = false)
    private Integer id;

    @Column(name = "CODE_PERIODICITE", length = 10)
    private String codePeriodicite;

    @Column(name = "LIBELLE_PERIODICITE", length = 15)
    private String libellePeriodicite;


}