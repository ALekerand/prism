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
@Table(name = "fonction")
@AutoCode(field = "codeFonction")
@EntityListeners(AutoCodeEntityListener.class)
public class Fonction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FONCTION", nullable = false)
    private Integer id;

    @Size(max = 20)
    @Column(name = "CODE_FONCTION", length = 20)
    private String codeFonction;

    @Size(max = 100)
    @Column(name = "LIBELLE_FONCTION", length = 100)
    private String libelleFonction;

    /** ALPHA | CP | CEC | SIE — null = toutes. */
    @Size(max = 20)
    @Column(name = "TYPE_CENTRE", length = 20)
    private String typeCentre;

}