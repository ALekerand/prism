package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "type_alpha")
public class TypeAlpha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TYPE_ALPHA", nullable = false)
    private Integer id;

    //TODO [Reverse Engineering] generate columns from DB
}