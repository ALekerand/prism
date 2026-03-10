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
public class DiplomePersonnelDto {
  
    private Integer id;

  
    private Diplome idDiplome;

   
    private String libelleAutreDiplome;


}