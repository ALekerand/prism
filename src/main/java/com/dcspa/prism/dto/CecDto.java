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
public class CecDto {
  
    private Integer id;
    private Centre centre;
    private Integer idPeriodicite;
    private Integer idAutoriteAutorisation;
    private Integer idPromoteur;
    private String codeCentre;
    private Boolean autorisation;
    private String encadreurNonMena;
    private Boolean encadrerParMena;
    private Boolean estElectrifie;
    private Boolean aDeLeau;
    private Integer nombreVisite;
    private String libelleCec;


}