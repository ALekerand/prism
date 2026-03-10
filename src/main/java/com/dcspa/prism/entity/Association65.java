package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "association_65")
public class Association65 {
    @EmbeddedId
    private Association65Id id;

    @MapsId("idCentre")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Cec idCentre;

    @MapsId("idEffectifDebut8")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_EFFECTIF_DEBUT8", nullable = false)
    private EffectifCec idEffectifDebut8;


}