package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "diplome_personnel")
public class DiplomePersonnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DIPLOME_PERSONNEL", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_DIPLOME", nullable = false)
    private Diplome idDiplome;

    @Column(name = "LIBELLE_AUTRE_DIPLOME", length = 150)
    private String libelleAutreDiplome;


}