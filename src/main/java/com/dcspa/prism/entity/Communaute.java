package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "communaute")
public class Communaute {
    @Id
    @Column(name = "ID_PROMOTEUR", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROMOTEUR", nullable = false)
    private Personnemorale personnemorale;

    @Column(name = "LIBELLE_COMMUNAUTE", length = 100)
    private String libelleCommunaute;

    @Column(name = "CODE_PROMOTEUR", length = 50)
    private String codePromoteur;


}