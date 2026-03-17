package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "structure_formation_certification")
public class StructureFormationCertification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_STRUCTURE_FORMATION_CERTIFICATION", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "CODE_STRUCTURE_CERTIFICATION", length = 50)
    private String codeStructureCertification;

    @Size(max = 50)
    @Column(name = "LIBELLE_STRUCTURE_CERTIFICATION", length = 50)
    private String libelleStructureCertification;


}