package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "domaine_activite_alpha")
public class DomaineActiviteAlpha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DOMAINE_ACTIVITE_ALPHA", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_DOMAINE_ACTIVITE", nullable = false)
    private DomaineActivite idDomaineActivite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Alpha idCentre;

    @Column(name = "LIBELLE_DOMAINE_ACTIVITE_ALPHA", length = 50)
    private String libelleDomaineActiviteAlpha;


}