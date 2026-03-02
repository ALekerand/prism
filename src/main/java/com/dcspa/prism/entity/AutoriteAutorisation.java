package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "autorite_autorisation")
public class AutoriteAutorisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AUTORITE_AUTORISATION", nullable = false)
    private Integer id;

    @Column(name = "CODE_AUTORISATION", length = 50)
    private String codeAutorisation;

    @Column(name = "LIBELLE_AUTORITE_AUTORISATION", length = 100)
    private String libelleAutoriteAutorisation;


}