package com.dcspa.prism.entity;

import com.dcspa.prism.codegen.AutoCode;
import com.dcspa.prism.codegen.AutoCodeEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ministere")
@JsonIgnoreProperties({"personnemorale", "hibernateLazyInitializer", "handler"})
@AutoCode(field = "codePromoteur")
@EntityListeners(AutoCodeEntityListener.class)
public class Ministere {
    @Id
    @Column(name = "ID_PROMOTEUR", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROMOTEUR", nullable = false)
    private Personnemorale personnemorale;

    @Size(max = 100)
    @Column(name = "LIBELLE_MINISTERE", length = 100)
    private String libelleMinistere;

    @Size(max = 50)
    @Column(name = "CODE_PROMOTEUR", length = 50)
    private String codePromoteur;

    @Size(max = 100)
    @Column(name = "LIBELLE_PROMOTEUR", length = 100)
    private String libellePromoteur;

    @Size(max = 100)
    @Column(name = "DENOMINATION", length = 100)
    private String denomination;

    @Size(max = 100)
    @Column(name = "NOM_PROGRAMME", length = 100)
    private String nomProgramme;

    @Size(max = 100)
    @Column(name = "NOM_REPRESENTANT_LEGAL_STRUCTURE", length = 100)
    private String nomRepresentantLegalStructure;

    @Size(max = 10)
    @Column(name = "CONTACT", length = 10)
    private String contact;

    @Size(max = 100)
    @Column(name = "BOITE_POSTALE", length = 100)
    private String boitePostale;

    @Size(max = 100)
    @Column(name = "MAIL", length = 100)
    private String mail;


}