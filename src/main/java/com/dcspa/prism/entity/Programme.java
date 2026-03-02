package com.dcspa.prism.entity;

import jakarta.persistence.*;
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

    @Column(name = "CODE_PROGMERAM", length = 50)
    private String codeProgmeram;


}