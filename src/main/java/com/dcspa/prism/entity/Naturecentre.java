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
@Table(name = "naturecentre")
@AutoCode(field = "codeNatureCentre")
@EntityListeners(AutoCodeEntityListener.class)
public class Naturecentre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NATURECENTRE", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "CODE_NATURE_CENTRE", length = 50)
    private String codeNatureCentre;

    @Size(max = 100)
    @Column(name = "LIBELLE_NATURE_CENTRE", length = 100)
    private String libelleNatureCentre;


}