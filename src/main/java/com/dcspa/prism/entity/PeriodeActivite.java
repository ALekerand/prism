package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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

    @Size(max = 10)
    @Column(name = "CODE_PERIODE_ACTIVITE", length = 10)
    private String codePeriodeActivite;

    @Size(max = 50)
    @Column(name = "LIBELLE_PERIODE_ACTIVITE", length = 50)
    private String libellePeriodeActivite;


}