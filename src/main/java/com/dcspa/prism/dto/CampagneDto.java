package com.dcspa.prism.dto;

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
public class CampagneDto {

	private Integer id;
	private String codeCampagne;
	private LocalDate dateDebutCampagne;
	private LocalDate dateFinCampagne;
	private Boolean etatCampagne;
}
