package com.dcspa.prism.entity;

import com.dcspa.prism.codegen.AutoCode;
import com.dcspa.prism.codegen.AutoCodeEntityListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "designation")
@AutoCode(field = "codeDesignation")
@EntityListeners(AutoCodeEntityListener.class)
public class Designation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DESIGNATION", nullable = false)
    private Integer id;

    @Size(max = 20)
    @Column(name = "CODE_DESIGNATION", length = 20)
    private String codeDesignation;

    @Size(max = 50)
    @Column(name = "LIBELLE_DESIGNATION", length = 50)
    private String libelleDesignation;


}