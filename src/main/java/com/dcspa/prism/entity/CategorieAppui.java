package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categorie_appui")
public class CategorieAppui {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CATEGORIE_APPUI", nullable = false)
    private Integer id;

    @Column(name = "CODE_CATEGORIE_APPUI", length = 50)
    private String codeCategorieAppui;

    @Column(name = "LIBELLE_CATEGORIE_APPUI", length = 100)
    private String libelleCategorieAppui;


}