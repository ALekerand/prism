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
@Table(name = "partenaire")
@AutoCode(field = "codePartenaire")
@EntityListeners(AutoCodeEntityListener.class)
public class Partenaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PARTENAIRE", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "CODE_PARTENAIRE", length = 50)
    private String codePartenaire;

    @Size(max = 100)
    @Column(name = "LIBELLE_PARTENAIRE", length = 100)
    private String libellePartenaire;


}