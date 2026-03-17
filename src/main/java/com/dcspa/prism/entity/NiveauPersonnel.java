package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "niveau_personnel")
public class NiveauPersonnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NIVEAU_PERSONNEL", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "CODE_NIVEAU_PERSONNEL", length = 50)
    private String codeNiveauPersonnel;

    @Size(max = 100)
    @Column(name = "LIBELLE_NIVEAU_PERSONNEL", length = 100)
    private String libelleNiveauPersonnel;


}