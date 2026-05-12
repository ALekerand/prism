package com.dcspa.prism.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "region")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REGION", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "CODE_REGION", length = 10)
    private String codeRegion;

    @Size(max = 100)
    @Column(name = "LIBELLE_REGION", length = 100)
    private String libelleRegion;
}
