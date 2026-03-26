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
@Table(name = "civilite")
@AutoCode(field = "codeCivilite")
@EntityListeners(AutoCodeEntityListener.class)
public class Civilite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CIVILITE", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "CODE_CIVILITE", length = 10)
    private String codeCivilite;

    @Size(max = 10)
    @Column(name = "LIBELLE_CIVILITE", length = 10)
    private String libelleCivilite;


}