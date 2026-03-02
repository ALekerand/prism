package com.dcspa.prism.entity;

import jakarta.persistence.*;
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

    @Column(name = "CODE_TYPE_DOCUMENT", length = 50)
    private String codeTypeDocument;


}