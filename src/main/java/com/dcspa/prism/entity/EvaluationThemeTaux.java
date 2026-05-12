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
@Table(name = "evaluation_theme_taux")
public class EvaluationThemeTaux {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_EVALUATION_THEME_TAUX", nullable = false)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_EVALUATION", nullable = false)
	private Evaluation evaluation;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_THEME_EVALUATION", nullable = false)
	private ThemeEvaluation themeEvaluation;

	@NotNull
	@Column(name = "TAUX", nullable = false)
	private Integer taux;
}
