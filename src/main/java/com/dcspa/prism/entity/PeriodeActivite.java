package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "periode_activite")
public class PeriodeActivite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERIODE_ACTIVITE", nullable = false)
    private Integer id;

    @Column(name = "CODE_PERIODE_ACTIVITE", length = 10)
    private String codePeriodeActivite;

    @Column(name = "LIBELLE_PERIODE_ACTIVITE", length = 50)
    private String libellePeriodeActivite;


}