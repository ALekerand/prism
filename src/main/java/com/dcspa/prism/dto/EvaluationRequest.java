package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationRequest {
	private Integer idAlpha;
	private Integer idPeriodeEvaluation;
	private Integer idNiveauEvaluation;
	private Integer idThemeEvaluationNiveau1;
	private Integer idThemeEvaluationN2PostAlpha;
	private Integer idTauxEvaluation;
	private Integer idAspectAAmeliorer;
	private String themeEvaluation;
}
