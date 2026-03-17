package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentRequest {
    private Integer idNatureDocument;
    private Integer idTypeDocument;
    private Integer idCentre;

    private String existe;
    private String ajour;
    private String bientenu;
    private String respmethode;
    private String bienrensigne;
    private String codeDocument;
}

