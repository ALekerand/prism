package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "promoteur")
public class Promoteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROMOTEUR", nullable = false)
    private Integer id;

    @Column(name = "CODE_PROMOTEUR", length = 50)
    private String codePromoteur;


}