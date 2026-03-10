package com.dcspa.prism.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonnelDto {
   
    private Integer id;

 
    private NiveauPersonnel idNiveauPersonnel;

 
    private DiplomePersonnel idDiplomePersonnel;

    private Fonction idFonction;

   
    private Civilite idCivilite;

  
    private Centre idCentre;

   
    private StructureFormationCertification idStructureFormationCertification;

  
    private StatutPersonnel idStatutPersonnel;

    
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