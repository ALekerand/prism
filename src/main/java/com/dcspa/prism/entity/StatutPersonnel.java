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
@Table(name = "statut_personnel")
@AutoCode(field = "codeStatutPersonnel")
@EntityListeners(AutoCodeEntityListener.class)
public class StatutPersonnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_STATUT_PERSONNEL", nullable = false)
    private Integer id;

    @Size(max = 40)
    @Column(name = "CODE_STATUT_PERSONNEL", length = 40)
    private String codeStatutPersonnel;

    @Size(max = 50)
    @Column(name = "LIBELLE_STATUT_PERSONNEL", length = 50)
    private String libelleStatutPersonnel;


}