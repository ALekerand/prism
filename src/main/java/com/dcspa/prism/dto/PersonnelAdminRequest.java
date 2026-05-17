package com.dcspa.prism.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonnelAdminRequest {
    private Integer idNiveauPersonnelId;
    private Integer idFonctionId;
    private Integer idCiviliteId;
    private Integer idCentreId;
    private Integer idStructureFormationCertificationId;
    private Integer idStatutPersonnelId;
    private Integer idDiplomeId;

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

