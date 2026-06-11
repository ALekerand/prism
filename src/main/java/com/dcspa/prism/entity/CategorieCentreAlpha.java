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
@Table(name = "categorie_centre_alpha")
@AutoCode(field = "codeCategorieCentreAlpha")
@EntityListeners(AutoCodeEntityListener.class)
public class CategorieCentreAlpha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CATEGORIE_CENTRE_ALPHA", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "CODE_CATEGORIE_CENTRE_ALPHA", length = 10)
    private String codeCategorieCentreAlpha;

    @Size(max = 30)
    @Column(name = "LIBELLE_CATEGORIE_CENTRE_ALPHA", length = 30)
    private String libelleCategorieCentreAlpha;


}