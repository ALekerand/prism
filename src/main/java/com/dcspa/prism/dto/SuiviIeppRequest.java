package com.dcspa.prism.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuiviIeppRequest {
	private Integer idAlpha;
	private Integer idPeriodeActivite;
	private Integer nombreVisiteEffectueParIepp;
	private Integer nombreReunionPointActiviteAlpha;
}
