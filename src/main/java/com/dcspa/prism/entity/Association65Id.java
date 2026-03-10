package com.dcspa.prism.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class Association65Id implements Serializable {
    private static final long serialVersionUID = 85110068209646507L;
    @Column(name = "ID_CENTRE", nullable = false)
    private Integer idCentre;

    @Column(name = "ID_EFFECTIF_DEBUT8", nullable = false)
    private Integer idEffectifDebut8;


}