package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "support_didactique_alpha")
public class SupportDidactiqueAlpha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUPPORT_DIDACTIQUE_ALPHA", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_SUPPORT_DIDACTIQUE", nullable = false)
    private SupportDidactique idSupportDidactique;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Alpha idCentre;

    @Column(name = "LIBELLE_AUTRE_SUPPORT", length = 50)
    private String libelleAutreSupport;


}