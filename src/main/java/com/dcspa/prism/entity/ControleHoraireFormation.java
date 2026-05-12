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
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "controle_horaire_formation")
public class ControleHoraireFormation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_CONTROLE_HORAIRE", nullable = false)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_CONTROLE", nullable = false)
	private Controle controle;

	@NotNull
	@Column(name = "JOUR_SEMAINE", nullable = false, length = 20)
	private String jourSemaine;

	@NotNull
	@Column(name = "HEURE_DEBUT", nullable = false)
	private LocalTime heureDebut;

	@NotNull
	@Column(name = "HEURE_FIN", nullable = false)
	private LocalTime heureFin;
}
