package com.dcspa.prism.entity;

import jakarta.persistence.*;
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

    @Column(name = "CODE_STRUCTURE_CERTIFICATION", length = 50)
    private String codeStructureCertification;

    @Column(name = "LIBELLE_STRUCTURE_CERTIFICATION", length = 50)
    private String libelleStructureCertification;


}