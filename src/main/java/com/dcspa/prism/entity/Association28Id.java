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
public class Association28Id implements Serializable {
    private static final long serialVersionUID = 964380878393662872L;
    @Column(name = "ID_NATURECENTRE", nullable = false)
    private Integer idNaturecentre;

    @Column(name = "ID_CENTRE", nullable = false)
    private Integer idCentre;


}