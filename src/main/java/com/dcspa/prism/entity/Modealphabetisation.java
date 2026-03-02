package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "modealphabetisation")
public class Modealphabetisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MODEALPHA", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Alpha idCentre;

    @Column(name = "CODE_MODEALPHA", length = 50)
    private String codeModealpha;

    @Column(name = "LIBELLE_MODEALPHA", length = 100)
    private String libelleModealpha;


}