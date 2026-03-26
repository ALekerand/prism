package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunauteRequest {
    private Integer idPromoteur;
    private String libelleCommunaute;
    private String codePromoteur;
    private String libellePromoteur;
    private String denomination;
    private String nomProgramme;
    private String nomRepresentantLegalStructure;
    private String contact;
    private String boitePostale;
    private String mail;
}
