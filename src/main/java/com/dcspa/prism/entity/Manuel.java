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
@Table(name = "manuel")
@AutoCode(field = "codeManuel")
@EntityListeners(AutoCodeEntityListener.class)
public class Manuel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_MANUEL", nullable = false)
	private Integer id;

	@Size(max = 50)
	@Column(name = "CODE_MANUEL", length = 50)
	private String codeManuel;

	@Size(max = 100)
	@Column(name = "LIBELLE_MANUEL", length = 100)
	private String libelleManuel;
}
