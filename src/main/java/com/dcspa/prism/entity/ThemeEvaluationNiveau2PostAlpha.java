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
@Table(name = "theme_evaluation_niveau2_post_alpha")
@AutoCode(field = "codeThemeEvaluationNiveau2PostAlpha")
@EntityListeners(AutoCodeEntityListener.class)
public class ThemeEvaluationNiveau2PostAlpha {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_THEME_EVALUATION_N2_POST_ALPHA", nullable = false)
	private Integer id;

	@Size(max = 50)
	@Column(name = "CODE_THEME_EVALUATION_N2_POST_ALPHA", length = 50)
	private String codeThemeEvaluationNiveau2PostAlpha;

	@Size(max = 120)
	@Column(name = "LIBELLE_THEME_EVALUATION_N2_POST_ALPHA", length = 120)
	private String libelleThemeEvaluationNiveau2PostAlpha;
}
