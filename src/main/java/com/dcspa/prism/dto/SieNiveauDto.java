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
public class SieNiveauDto {
  
    private Integer id;

  
    private NiveauSieCec idNiveauSie;

 
    private AnneScolaire idAnneeScolaire;

 
    private Sie idCentre;

   
    private String codeSieNiveau;

   
    private Integer nombreSalleSie;


}