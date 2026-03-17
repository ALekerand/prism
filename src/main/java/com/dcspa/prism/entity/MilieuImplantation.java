package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "milieu_implantation")
public class MilieuImplantation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MILIEU_IMPLENTATION", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "CODE_MILIEU_IMPLENTATION", length = 10)
    private String codeMilieuImplentation;

    @Size(max = 10)
    @Column(name = "LIBELLE_TYPE_IMPLENTATION_", length = 10)
    private String libelleTypeImplentation;


}