package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "niveau_alpha")
public class NiveauAlpha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NIVEAU_ALPHA", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Alpha idCentre;

    @Column(name = "CODE_NIVEAU_ALPHA", length = 50)
    private String codeNiveauAlpha;

    @Column(name = "LIBELLE_NIVEAU_ALPHA", length = 100)
    private String libelleNiveauAlpha;


}