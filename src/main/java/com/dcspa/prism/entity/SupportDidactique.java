package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "support_didactique")
public class SupportDidactique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUPPORT_DIDACTIQUE", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "LIBELLE_SUPPORT_DIDACTIQUE", length = 50)
    private String libelleSupportDidactique;


}