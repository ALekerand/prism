package com.dcspa.prism.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "suivi_iepp")
public class SuiviIepp {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_SUIVI_IEPP", nullable = false)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_ALPHA", nullable = false)
	private Alpha idAlpha;

	@Column(name = "NOMBRE_VISITE_EFFECTUE_PAR_IEPP")
	private Integer nombreVisiteEffectueParIepp;

	@Column(name = "NOMBRE_REUNION_POINT_ACTIVITE_ALPHA")
	private Integer nombreReunionPointActiviteAlpha;

	@Column(name = "VALIDEE_IEPP")
	private Boolean valideeIepp;
}
