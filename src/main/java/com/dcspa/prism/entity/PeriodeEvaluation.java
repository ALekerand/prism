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
@Table(name = "periode_evaluation")
@AutoCode(field = "codePeriodeEvaluation")
@EntityListeners(AutoCodeEntityListener.class)
public class PeriodeEvaluation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PERIODE_EVALUATION", nullable = false)
	private Integer id;

	@Size(max = 50)
	@Column(name = "CODE_PERIODE_EVALUATION", length = 50)
	private String codePeriodeEvaluation;

	@Size(max = 100)
	@Column(name = "LIBELLE_PERIODE_EVALUATION", length = 100)
	private String libellePeriodeEvaluation;
}
