package com.dcspa.prism.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationRequest {
	private Integer idAlpha;
	private Integer idPeriodeActivite;
	private Integer idNiveauEvaluation;
	private Integer idThemeEvaluation;
	private Integer idTauxEvaluation;
	private String typeEvaluation;
	private List<ThemeTauxRequest> themesTaux;

	@Getter
	@Setter
	public static class ThemeTauxRequest {
		private Integer idThemeEvaluation;
		/** Conservé pour compatibilité ; recalculé si total/obtenu fournis. */
		private Integer taux;
		private Integer nombreTotalEvalue;
		private Integer nombreResultatObtenu;
	}
}
