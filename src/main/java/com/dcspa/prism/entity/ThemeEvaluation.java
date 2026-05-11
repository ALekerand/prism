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
@Table(name = "theme_evaluation")
@AutoCode(field = "codeThemeEvaluation")
@EntityListeners(AutoCodeEntityListener.class)
public class ThemeEvaluation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_THEME_EVALUATION", nullable = false)
	private Integer id;

	@Size(max = 50)
	@Column(name = "CODE_THEME_EVALUATION", length = 50)
	private String codeThemeEvaluation;

	@Size(max = 200)
	@Column(name = "LIBELLE_THEME_EVALUATION", length = 200)
	private String libelleThemeEvaluation;

	/**
	 * Valeurs attendues : "NIVEAU_1", "NIVEAU_2", "POST_ALPHA".
	 */
	@Size(max = 20)
	@Column(name = "NIVEAU", length = 20)
	private String niveau;
}
