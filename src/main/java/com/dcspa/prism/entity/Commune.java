package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "commune")
public class Commune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COMMUNE", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "CODE_COMMUNE", length = 10)
    private String codeCommune;

    @Size(max = 30)
    @Column(name = "NOM_COMMUNE", length = 30)
    private String nomCommune;


}