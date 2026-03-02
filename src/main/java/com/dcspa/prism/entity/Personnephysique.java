package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "personnephysique")
public class Personnephysique {
    @Id
    @Column(name = "ID_PROMOTEUR", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROMOTEUR", nullable = false)
    private Promoteur promoteur;

    @Column(name = "CODE_PROMOTEUR", length = 50)
    private String codePromoteur;

    @Column(name = "CODE_PERSONNE_PHYSIQUE", length = 50)
    private String codePersonnePhysique;

    @Column(name = "LIBELLE_PERSONNE_PHYSIQUE", length = 100)
    private String libellePersonnePhysique;


}