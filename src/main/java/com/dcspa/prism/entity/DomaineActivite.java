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
@Table(name = "domaine_activite")
@AutoCode(field = "codeDomaineActivite")
@EntityListeners(AutoCodeEntityListener.class)
public class DomaineActivite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DOMAINE_ACTIVITE", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "CODE_DOMAINE_ACTIVITE", length = 50)
    private String codeDomaineActivite;

    @Size(max = 100)
    @Column(name = "LIBELLE_DOMAINE_ACTIVITE", length = 100)
    private String libelleDomaineActivite;


}