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
@Table(name = "performance")
public class Performance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PERFORMANCE", nullable = false)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_ALPHA", nullable = false)
	private Alpha idAlpha;

	@Column(name = "TAUX_FREQUENTATION_PAR_MOIS")
	private String tauxFrequentationParMois;

	@Column(name = "TAUX_PROGRESSION_APPRENTISSAGE_LECTURE")
	private String tauxProgressionApprentissageLecture;

	@Column(name = "TAUX_PROGRESSION_APPRENTISSAGE_ECRITURE")
	private String tauxProgressionApprentissageEcriture;

	@Column(name = "TAUX_PROGRESSION_APPRENTISSAGE_CALCUL")
	private String tauxProgressionApprentissageCalcul;

	@Column(name = "TAUX_PROGRESSION_APPRENTISSAGE_CVC")
	private String tauxProgressionApprentissageCvc;
}
