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
@Table(name = "source_financement")
@AutoCode(field = "codeSourceFinancement")
@EntityListeners(AutoCodeEntityListener.class)
public class SourceFinancement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_SOURCE_FINANCEMENT", nullable = false)
	private Integer id;

	@Size(max = 20)
	@Column(name = "CODE_SOURCE_FINANCEMENT", length = 20)
	private String codeSourceFinancement;

	@Size(max = 50)
	@Column(name = "LIBELLE_SOURCE_FINANCEMENT", length = 50)
	private String libelleSourceFinancement;
}
