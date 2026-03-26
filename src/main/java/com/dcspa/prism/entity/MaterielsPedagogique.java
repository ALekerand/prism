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
@Table(name = "materiels_pedagogique")
@AutoCode(field = "codeMaterielPedagogique")
@EntityListeners(AutoCodeEntityListener.class)
public class MaterielsPedagogique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATERIEL_PEDAGOGIQUE", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "CODE_MATERIEL_PEDAGOGIQUE", length = 10)
    private String codeMaterielPedagogique;

    @Size(max = 50)
    @Column(name = "LIBELLE_MATERIEL_PEDAGOGIQUE", length = 50)
    private String libelleMaterielPedagogique;


}