package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_DOMAINE_ACTIVITE", nullable = false)
    private DomaineActivite idDomaineActivite;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Alpha idCentre;

    @Size(max = 50)
    @Column(name = "LIBELLE_DOMAINE_ACTIVITE_ALPHA", length = 50)
    private String libelleDomaineActiviteAlpha;


}