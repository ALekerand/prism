package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role_fonctionnalite_permission", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ID_ROLE", "ID_FONCTIONNALITE", "ID_PERMISSION"})
})
public class RoleFonctionnalitePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ROLE", nullable = false)
    private AppRole role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_FONCTIONNALITE", nullable = false)
    private Fonctionnalite fonctionnalite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PERMISSION", nullable = false)
    private Permission permission;
}
