package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "type_document")
public class TypeDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TYPE_DOCUMENT", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "CODE_TYPE_DOCUMENT", length = 50)
    private String codeTypeDocument;

    @Size(max = 100)
    @Column(name = "LIBELLE_TYPE_DOCUMENT", length = 100)
    private String libelleTypeDocument;


}