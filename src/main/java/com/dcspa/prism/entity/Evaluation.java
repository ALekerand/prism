package com.dcspa.prism.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "evaluation")
public class Evaluation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_EVALUATION", nullable = false)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_ALPHA", nullable = false)
	private Alpha idAlpha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERIODE_EVALUATION")
	private PeriodeEvaluation idPeriodeEvaluation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_NIVEAU_EVALUATION")
	private NiveauEvaluation idNiveauEvaluation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_THEME_EVALUATION_NIVEAU1")
	private ThemeEvaluationNiveau1 idThemeEvaluationNiveau1;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_THEME_EVALUATION_N2_POST_ALPHA")
	private ThemeEvaluationNiveau2PostAlpha idThemeEvaluationN2PostAlpha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TAUX_EVALUATION")
	private TauxEvaluation idTauxEvaluation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ASPECT_A_AMELIORER")
	private AspectAAmeliorer idAspectAAmeliorer;

	@Column(name = "THEME_EVALUATION", length = 255)
	private String themeEvaluation;
}
