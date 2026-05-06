package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerformanceRequest {
	private Integer idAlpha;
	private String tauxFrequentationParMois;
	private String tauxProgressionApprentissageLecture;
	private String tauxProgressionApprentissageEcriture;
	private String tauxProgressionApprentissageCalcul;
	private String tauxProgressionApprentissageCvc;
}
