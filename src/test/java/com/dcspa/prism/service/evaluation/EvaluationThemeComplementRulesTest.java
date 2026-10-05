package com.dcspa.prism.service.evaluation;

import com.dcspa.prism.dto.EvaluationRequest;
import com.dcspa.prism.entity.ThemeEvaluation;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationThemeComplementRulesTest {

	@Test
	void familyKeyPairsSaitLireWithNeSaitPasLire() {
		assertThat(EvaluationThemeComplementRules.familyKey("Sait lire", null)).isEqualTo("SAIT_LIRE");
		assertThat(EvaluationThemeComplementRules.familyKey("Ne sait pas lire", null)).isEqualTo("SAIT_LIRE");
		assertThat(EvaluationThemeComplementRules.isComplementary("Ne sait pas lire", null)).isTrue();
	}

	@Test
	void familyKeyPairsLivretComplements() {
		assertThat(EvaluationThemeComplementRules.familyKey("Sait écrire", null))
				.isEqualTo(EvaluationThemeComplementRules.familyKey("Ne sait pas écrire", null));
		assertThat(EvaluationThemeComplementRules.familyKey("Sait calculer", null))
				.isEqualTo(EvaluationThemeComplementRules.familyKey("Ne sait calculer", null));
		assertThat(EvaluationThemeComplementRules.familyKey("Comprend un texte", null))
				.isEqualTo(EvaluationThemeComplementRules.familyKey("Ne comprend pas un texte", null));
		assertThat(EvaluationThemeComplementRules.familyKey("Résoud une situation problème", null))
				.isEqualTo(EvaluationThemeComplementRules.familyKey("Ne Résoud une situation problème", null));
	}

	@Test
	void applyComplementsDeducesNegativeFromPositive() {
		EvaluationRequest.ThemeTauxRequest saitLire = request(1, 40, 25);
		EvaluationRequest.ThemeTauxRequest neSaitPasLire = request(2, 1, 1);
		ThemeEvaluation positif = theme(1, "TEV-N1-SAIT-LIRE", "Sait lire");
		ThemeEvaluation negatif = theme(2, "TEV-N1-NE-SAIT-PAS-LIRE", "Ne sait pas lire");

		EvaluationThemeComplementRules.applyComplements(
				List.of(saitLire, neSaitPasLire),
				id -> id == 1 ? positif : negatif);

		assertThat(neSaitPasLire.getNombreTotalEvalue()).isEqualTo(40);
		assertThat(neSaitPasLire.getNombreResultatObtenu()).isEqualTo(15);
		assertThat(saitLire.getNombreResultatObtenu()).isEqualTo(25);
	}

	private static EvaluationRequest.ThemeTauxRequest request(int themeId, int total, int obtenu) {
		EvaluationRequest.ThemeTauxRequest item = new EvaluationRequest.ThemeTauxRequest();
		item.setIdThemeEvaluation(themeId);
		item.setNombreTotalEvalue(total);
		item.setNombreResultatObtenu(obtenu);
		return item;
	}

	private static ThemeEvaluation theme(int id, String code, String libelle) {
		ThemeEvaluation theme = new ThemeEvaluation();
		theme.setId(id);
		theme.setCodeThemeEvaluation(code);
		theme.setLibelleThemeEvaluation(libelle);
		return theme;
	}
}
