package com.dcspa.prism.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PersonnelAdminResponse {
    private Integer id;

    private Integer niveauPersonnelId;
    private Integer fonctionId;
    private Integer civiliteId;
    private Integer centreId;
    private Integer structureFormationCertificationId;
    private Integer statutPersonnelId;
    private Integer diplomeId;
    private String libelleAutreDiplome;

    private String codePersonnel;
    private Boolean certifierPersonnel;
    private String nomPersonnel;
    private String prenomsPersonnel;
    private Integer anneExpePersonnel;
    private String sexePersonnel;
    private LocalDate dateNaissance;
    private Integer ancienneFonctPromoPesonnel;
    private String contactPersonnel;
    private String boitePostalePersonnel;
    private String emailPersonnel;
    private String denominationPersonnel;
    private String nomDuPrgramme;
    private String nomRepresentantLegalSturcture;
}

