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
@Table(name = "impact")
@AutoCode(field = "codeImpact")
@EntityListeners(AutoCodeEntityListener.class)
public class Impact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_IMPACT", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "CODE_IMPACT", length = 10)
    private String codeImpact;

    @Size(max = 50)
    @Column(name = "LIBELLE_IMPACT", length = 50)
    private String libelleImpact;


}