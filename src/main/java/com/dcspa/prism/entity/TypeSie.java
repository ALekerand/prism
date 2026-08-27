package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "type_sie")
public class TypeSie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TYPE_SIE", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "LIBELLE_TYPE_SIE", length = 50)
    private String libelleTypeSie;
}
