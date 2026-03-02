package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "societe_civile")
public class SocieteCivile {
    @Id
    @Column(name = "ID_PROMOTEUR", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROMOTEUR", nullable = false)
    private Personnemorale personnemorale;

    @Column(name = "LIBELLE_SOCIETE_CIVILE", length = 100)
    private String libelleSocieteCivile;

    @Column(name = "CODE_PROMOTEUR", length = 50)
    private String codePromoteur;


}