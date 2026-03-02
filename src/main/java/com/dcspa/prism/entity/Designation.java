package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "designation")
public class Designation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DESIGNATION", nullable = false)
    private Integer id;

    @Column(name = "CODE_DESIGNATION", length = 20)
    private String codeDesignation;

    @Column(name = "LIBELLE_DESIGNATION", length = 50)
    private String libelleDesignation;


}