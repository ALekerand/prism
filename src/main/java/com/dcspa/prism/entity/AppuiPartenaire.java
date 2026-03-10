package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "appui_partenaire")
public class AppuiPartenaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_APPUI_PARTENAIRE", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CATEGORIE_APPUI", nullable = false)
    private CategorieAppui idCategorieAppui;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Centre idCentre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PARTENAIRE", nullable = false)
    private Partenaire idPartenaire;

    @Column(name = "CODE_APPUI_PARTENAIRE", length = 20)
    private String codeAppuiPartenaire;


}