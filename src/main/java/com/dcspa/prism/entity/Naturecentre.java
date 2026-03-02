package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "naturecentre")
public class Naturecentre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NATURECENTRE", nullable = false)
    private Integer id;

    @Column(name = "CODE_NATURE_CENTRE", length = 50)
    private String codeNatureCentre;

    @Column(name = "LIBELLE_NATURE_CENTRE", length = 100)
    private String libelleNatureCentre;


}