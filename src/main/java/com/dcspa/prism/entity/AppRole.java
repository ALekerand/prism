package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "app_role")
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROLE", nullable = false)
    private Integer id;

    @Column(name = "CODE_ROLE", nullable = false, unique = true, length = 50)
    private String codeRole;

    @Column(name = "LIBELLE_ROLE", nullable = false, length = 100)
    private String libelleRole;

    @Column(name = "DESCRIPTION_ROLE", length = 255)
    private String descriptionRole;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<AppUser> users = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleFonctionnalitePermission> roleFonctionnalitePermissions = new HashSet<>();
}
