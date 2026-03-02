package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "domaine_activite")
public class DomaineActivite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DOMAINE_ACTIVITE", nullable = false)
    private Integer id;

    @Column(name = "CODE_DOMAINE_ACTIVITE", length = 50)
    private String codeDomaineActivite;

    @Column(name = "LIBELLE_DOMAINE_ACTIVITE", length = 100)
    private String libelleDomaineActivite;


}