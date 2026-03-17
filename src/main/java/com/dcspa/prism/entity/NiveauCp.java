package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "niveau_cp")
public class NiveauCp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NIVEAU_CP", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "LIBELLE_NIVEAU_CP", length = 100)
    private String libelleNiveauCp;


}