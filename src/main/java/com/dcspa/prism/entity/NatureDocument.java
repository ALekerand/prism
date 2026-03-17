package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "nature_document")
public class NatureDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NATURE_DOCUMENT", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "LIBELLE_NATURE_DOCUMENT_", length = 100)
    private String libelleNatureDocument;


}