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
@Table(name = "promoteur")
@AutoCode(field = "codePromoteur")
@EntityListeners(AutoCodeEntityListener.class)
public class Promoteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROMOTEUR", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "CODE_PROMOTEUR", length = 50)
    private String codePromoteur;

    @Size(max = 100)
    @Column(name = "LIBELLE_PROMOTEUR", length = 100)
    private String libellePromoteur;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE_PROMOTEUR", length = 20)
    private TypePromoteur typePromoteur;

}