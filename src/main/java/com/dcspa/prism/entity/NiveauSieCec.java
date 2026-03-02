package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "niveau_sie_cec")
public class NiveauSieCec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NIVEAU_SIE", nullable = false)
    private Integer id;

    @Column(name = "LIBELLE_NIVEAU_SIE", length = 100)
    private String libelleNiveauSie;

    @Column(name = "CODE_NIVEAU_SIE", length = 50)
    private String codeNiveauSie;


}