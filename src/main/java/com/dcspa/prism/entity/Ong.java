package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ong")
public class Ong {
    @Id
    @Column(name = "ID_PROMOTEUR", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROMOTEUR", nullable = false)
    private Personnemorale personnemorale;

    @Column(name = "LIBELLE_ONG", length = 100)
    private String libelleOng;

    @Column(name = "CODE_PROMOTEUR", length = 50)
    private String codePromoteur;


}