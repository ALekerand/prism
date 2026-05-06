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
@Table(name = "aspect_a_ameliorer")
@AutoCode(field = "codeAspectAAmeliorer")
@EntityListeners(AutoCodeEntityListener.class)
public class AspectAAmeliorer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_ASPECT_A_AMELIORER", nullable = false)
	private Integer id;

	@Size(max = 50)
	@Column(name = "CODE_ASPECT_A_AMELIORER", length = 50)
	private String codeAspectAAmeliorer;

	@Size(max = 200)
	@Column(name = "LIBELLE_ASPECT_A_AMELIORER", length = 200)
	private String libelleAspectAAmeliorer;
}
