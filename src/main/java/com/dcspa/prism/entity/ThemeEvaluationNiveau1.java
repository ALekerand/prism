package com.dcspa.prism.entity;

import com.dcspa.prism.codegen.AutoCode;
import com.dcspa.prism.codegen.AutoCodeEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "theme_evaluation_niveau1")
@AutoCode(field = "codeThemeEvaluationNiveau1")
@EntityListeners(AutoCodeEntityListener.class)
public class ThemeEvaluationNiveau1 {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_THEME_EVALUATION_NIVEAU1", nullable = false)
	private Integer id;

	@Size(max = 50)
	@Column(name = "CODE_THEME_EVALUATION_NIVEAU1", length = 50)
	private String codeThemeEvaluationNiveau1;

	@Size(max = 100)
	@Column(name = "LIBELLE_THEME_EVALUATION_NIVEAU1", length = 100)
	private String libelleThemeEvaluationNiveau1;
}
