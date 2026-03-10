package com.dcspa.prism.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnneScolaireDto {

    private Integer id;
    private String codeAnneeScolaire;
    private LocalDate debutAnneeScolaire;
    private LocalDate finAnneeScolaire;
    private Boolean etatAnneeScolaire;


}