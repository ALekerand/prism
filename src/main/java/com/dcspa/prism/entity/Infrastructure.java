package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "infrastructure")
public class Infrastructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TYPE_INFRASTRUCTURE", nullable = false)
    private Integer id;

    @Column(name = "CODE_TYPE_INFRASTRUCTURE", length = 50)
    private String codeTypeInfrastructure;


}