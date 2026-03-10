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
public class CompetenceDto {
   
    private Integer id;
    private String codeCompetence;
    private String libelleCompetence;


}