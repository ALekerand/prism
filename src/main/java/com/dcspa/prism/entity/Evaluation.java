package com.dcspa.prism.entity;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
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
	@JoinColumn(name = "ID_THEME_EVALUATION")
	private ThemeEvaluation idThemeEvaluation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TAUX_EVALUATION")
	private TauxEvaluation idTauxEvaluation;

	@Column(name = "TYPE_EVALUATION", length = 30)
	private String typeEvaluation;

	@OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EvaluationThemeTaux> themesTaux = new ArrayList<>();
}
