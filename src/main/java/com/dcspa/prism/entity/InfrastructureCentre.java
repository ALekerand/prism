package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "infrastructure_centre")
public class InfrastructureCentre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INFRASTRUCTURE_CENTRE", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Centre idCentre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_TYPE_INFRASTRUCTURE", nullable = false)
    private Infrastructure idTypeInfrastructure;

    @Column(name = "LIBELLE_AUTRE_INFRASTRUCTURE", length = 100)
    private String libelleAutreInfrastructure;


}