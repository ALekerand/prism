package com.dcspa.prism.entity;

import com.dcspa.prism.codegen.AutoCode;
import com.dcspa.prism.codegen.AutoCodeEntityListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "appui_partenaire")
@AutoCode(field = "codeAppuiPartenaire")
@EntityListeners(AutoCodeEntityListener.class)
public class AppuiPartenaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_APPUI_PARTENAIRE", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CATEGORIE_APPUI", nullable = false)
    private CategorieAppui idCategorieAppui;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Centre idCentre;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PARTENAIRE", nullable = false)
    private Partenaire idPartenaire;

    @Size(max = 20)
    @Column(name = "CODE_APPUI_PARTENAIRE", length = 20)
    private String codeAppuiPartenaire;

    @Size(max = 150)
    @Column(name = "LIBELLE_APPUI_PARTENAIRE", length = 150)
    private String libelleAppuiPartenaire;

}