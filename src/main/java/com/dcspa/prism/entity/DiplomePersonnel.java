package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_DIPLOME", nullable = false)
    private Diplome idDiplome;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PERSONNEL", nullable = false)
    private Personnel idPersonnel;

    @Size(max = 150)
    @Column(name = "LIBELLE_AUTRE_DIPLOME", length = 150)
    private String libelleAutreDiplome;


}