package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "programme")
public class Programme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROGRAMME", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "CODE_PROGRAMME", length = 50)
    private String codeProgramme;


}