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
public class MaterielAlphaDto {
   
    private Integer id;

    private MaterielsPedagogique idMaterielPedagogique;

    private Alpha idCentre;

    private String libelleAutreMateriel;


}