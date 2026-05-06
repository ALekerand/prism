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
@Table(name = "visite")
public class Visite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_VISITE", nullable = false)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_ALPHA", nullable = false)
	private Alpha idAlpha;

	@Column(name = "MAITRISE_SEANCE_LECTURE")
	private String maitriseSeanceLecture;

	@Column(name = "MAITRISE_SEANCE_ECRITURE")
	private String maitriseSeanceEcriture;

	@Column(name = "MAITRISE_SEANCE_CALCUL")
	private String maitriseSeanceCalcul;

	@Column(name = "MAITRISE_SEANCE_CVC")
	private String maitriseSeanceCvc;

	@Column(name = "NOMBRE_VISITE_REALISE_PAR_CONSEILLER")
	private Integer nombreVisiteRealiseParConseiller;

	@Column(name = "NOMBRE_BULLETIN_EFFECTUE_PAR_CONSEILLER")
	private Integer nombreBulletinEffectueParConseiller;

	@Column(name = "NOMBRE_VISITE_CONSEILLER_SUPERVISEUR_EFFECTUE")
	private Integer nombreVisiteConseillerSuperviseurEffectue;

	@Column(name = "NOMBRE_REUNION_BILAN_CONSEILLER_SUPERVISEUR")
	private Integer nombreReunionBilanConseillerSuperviseur;

	@Column(name = "NOMBRE_VISITE_EFFECTUE_PAR_IEPP")
	private Integer nombreVisiteEffectueParIepp;

	@Column(name = "NOMBRE_REUNION_POINT_ACTIVITE_ALPHA")
	private Integer nombreReunionPointActiviteAlpha;
}
