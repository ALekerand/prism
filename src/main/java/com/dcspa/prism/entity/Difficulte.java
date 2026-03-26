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
@Table(name = "difficulte")
@AutoCode(field = "codeDifficulte")
@EntityListeners(AutoCodeEntityListener.class)
public class Difficulte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DIFFICULTE", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "CODE_DIFFICULTE", length = 10)
    private String codeDifficulte;

    @Size(max = 50)
    @Column(name = "LIBELLE_DIFFICULTE", length = 50)
    private String libelleDifficulte;


}