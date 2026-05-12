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
@Table(name = "suivi_superviseur")
public class SuiviSuperviseur {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_SUIVI_SUPERVISEUR", nullable = false)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_ALPHA", nullable = false)
	private Alpha idAlpha;

	@Column(name = "NOMBRE_VISITE_SUPERVISEUR_EFFECTUE")
	private Integer nombreVisiteConseillerSuperviseurEffectue;

	@Column(name = "NOMBRE_REUNION_BILAN_SUPERVISEUR")
	private Integer nombreReunionBilanConseillerSuperviseur;

	@Column(name = "VALIDEE_SUPERVISEUR")
	private Boolean valideeSuperviseur;
}
