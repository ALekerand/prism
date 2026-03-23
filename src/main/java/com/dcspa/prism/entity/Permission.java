package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERMISSION", nullable = false)
    private Integer id;

    @Column(name = "CODE_PERMISSION", nullable = false, unique = true, length = 50)
    private String codePermission;

    @Column(name = "LIBELLE_PERMISSION", nullable = false, length = 100)
    private String libellePermission;
}
