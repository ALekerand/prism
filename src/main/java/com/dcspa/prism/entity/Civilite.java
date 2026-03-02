package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "civilite")
public class Civilite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CIVILITE", nullable = false)
    private Integer id;

    @Column(name = "CODE_CIVILITE", length = 10)
    private String codeCivilite;

    @Column(name = "LIBELLE_CIVILITE", length = 10)
    private String libelleCivilite;


}