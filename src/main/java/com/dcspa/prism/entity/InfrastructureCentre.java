package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Centre idCentre;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_INFRASTRUCTURE", nullable = false)
    private Infrastructure idInfrastructure;

    @Size(max = 100)
    @Column(name = "LIBELLE_AUTRE_INFRASTRUCTURE", length = 100)
    private String libelleAutreInfrastructure;


}