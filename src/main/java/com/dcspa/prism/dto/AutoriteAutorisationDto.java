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
public class AutoriteAutorisationDto {
  
    private Integer id;
    private String codeAutorisation;
    private String libelleAutoriteAutorisation;


}