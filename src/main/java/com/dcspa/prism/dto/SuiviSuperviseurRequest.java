package com.dcspa.prism.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuiviSuperviseurRequest {
	private Integer idAlpha;
	private Integer nombreVisiteConseillerSuperviseurEffectue;
	private Integer nombreReunionBilanConseillerSuperviseur;
}
