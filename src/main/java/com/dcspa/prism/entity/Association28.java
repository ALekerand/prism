package com.dcspa.prism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "association_28")
public class Association28 {
    @EmbeddedId
    private Association28Id id;

    @MapsId("idNaturecentre")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_NATURECENTRE", nullable = false)
    private Naturecentre idNaturecentre;

    @MapsId("idCentre")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Centre idCentre;


}