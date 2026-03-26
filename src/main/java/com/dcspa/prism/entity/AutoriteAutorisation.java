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
@Table(name = "autorite_autorisation")
@AutoCode(field = "codeAutorisation")
@EntityListeners(AutoCodeEntityListener.class)
public class AutoriteAutorisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AUTORITE_AUTORISATION", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "CODE_AUTORISATION", length = 50)
    private String codeAutorisation;

    @Size(max = 100)
    @Column(name = "LIBELLE_AUTORITE_AUTORISATION", length = 100)
    private String libelleAutoriteAutorisation;


}