package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationRequest {
	private Integer idAlpha;
	private Integer idPeriodeEvaluation;
	private Integer idNiveauEvaluation;
	private Integer idThemeEvaluation;
	private Integer idTauxEvaluation;
}
