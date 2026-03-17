package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DOCUMENT", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NATURE_DOCUMENT", nullable = false)
    private NatureDocument idNatureDocument;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_TYPE_DOCUMENT", nullable = false)
    private TypeDocument idTypeDocument;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Alpha idCentre;

    @Size(max = 5)
    @Column(name = "EXISTE", length = 5)
    private String existe;

    @Size(max = 5)
    @Column(name = "AJOUR", length = 5)
    private String ajour;

    @Size(max = 5)
    @Column(name = "BIENTENU", length = 5)
    private String bientenu;

    @Size(max = 5)
    @Column(name = "RESPMETHODE", length = 5)
    private String respmethode;

    @Size(max = 5)
    @Column(name = "BIENRENSIGNE", length = 5)
    private String bienrensigne;

    @Size(max = 50)
    @Column(name = "CODE_DOCUMENT", length = 50)
    private String codeDocument;


}