package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "statut_personnel")
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