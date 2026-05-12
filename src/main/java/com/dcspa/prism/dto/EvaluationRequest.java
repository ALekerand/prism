package com.dcspa.prism.dto;

import java.util.List;
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
	private String typeEvaluation;
	private List<ThemeTauxRequest> themesTaux;

	@Getter
	@Setter
	public static class ThemeTauxRequest {
		private Integer idThemeEvaluation;
		private Integer taux;
	}
}
