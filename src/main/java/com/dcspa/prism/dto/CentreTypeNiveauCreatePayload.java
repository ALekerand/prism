package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CentreTypeNiveauCreatePayload {
    private Integer anneeScolaireId;
    private Integer niveauId;
    private Integer nombreSalle;
    private String codeNiveau;
}
