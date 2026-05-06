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
@Table(name = "discipline")
@AutoCode(field = "codeDiscipline")
@EntityListeners(AutoCodeEntityListener.class)
public class Discipline {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_DISCIPLINE", nullable = false)
	private Integer id;

	@Size(max = 50)
	@Column(name = "CODE_DISCIPLINE", length = 50)
	private String codeDiscipline;

	@Size(max = 100)
	@Column(name = "LIBELLE_DISCIPLINE", length = 100)
	private String libelleDiscipline;
}
