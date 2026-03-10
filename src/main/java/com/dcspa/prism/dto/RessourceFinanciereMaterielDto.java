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
public class RessourceFinanciereMaterielDto {
 
    private Integer id;

  
    private Centre idCentre;

   
    private Designation idDesignation;

 
    private String sourceFinancement;

  
    private BigDecimal montant;


}