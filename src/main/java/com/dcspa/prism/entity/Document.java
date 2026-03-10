package com.dcspa.prism.entity;

import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NATURE_DOCUMENT", nullable = false)
    private NatureDocument idNatureDocument;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_TYPE_DOCUMENT", nullable = false)
    private TypeDocument idTypeDocument;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Alpha idCentre;

    @Column(name = "EXISTE", length = 5)
    private String existe;

    @Column(name = "AJOUR", length = 5)
    private String ajour;

    @Column(name = "BIENTENU", length = 5)
    private String bientenu;

    @Column(name = "RESPMETHODE", length = 5)
    private String respmethode;

    @Column(name = "BIENRENSIGNE", length = 5)
    private String bienrensigne;

    @Column(name = "CODE_DOCUMENT", length = 50)
    private String codeDocument;


}