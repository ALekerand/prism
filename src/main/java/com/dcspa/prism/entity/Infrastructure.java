package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "infrastructure")
public class Infrastructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INFRASTRUCTURE", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "CODE_INFRASTRUCTURE", length = 50)
    private String codeInfrastructure;

    @Size(max = 100)
    @Column(name = "LIBELLE_INFRASTRUCTURE", length = 100)
    private String libelleInfrastructure;


}