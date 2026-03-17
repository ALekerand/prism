package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "departement")
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DEPARTEMENT", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "CODE_DEPARTEMENT", length = 10)
    private String codeDepartement;

    @Size(max = 30)
    @Column(name = "NOM_DEPARTEMENT", length = 30)
    private String nomDepartement;


}