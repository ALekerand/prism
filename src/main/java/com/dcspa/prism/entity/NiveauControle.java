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
@Table(name = "niveau_controle")
@AutoCode(field = "codeNiveauControle")
@EntityListeners(AutoCodeEntityListener.class)
public class NiveauControle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_NIVEAU_CONTROLE", nullable = false)
	private Integer id;

	@Size(max = 50)
	@Column(name = "CODE_NIVEAU_CONTROLE", length = 50)
	private String codeNiveauControle;

	@Size(max = 100)
	@Column(name = "LIBELLE_NIVEAU_CONTROLE", length = 100)
	private String libelleNiveauControle;
}
