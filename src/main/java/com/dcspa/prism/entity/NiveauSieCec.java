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
@Table(name = "niveau_sie_cec")
@AutoCode(field = "codeNiveauSie")
@EntityListeners(AutoCodeEntityListener.class)
public class NiveauSieCec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NIVEAU_SIE", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "LIBELLE_NIVEAU_SIE", length = 100)
    private String libelleNiveauSie;

    @Size(max = 50)
    @Column(name = "CODE_NIVEAU_SIE", length = 50)
    private String codeNiveauSie;


}