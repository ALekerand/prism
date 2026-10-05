package com.dcspa.prism.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/** Étape durable du circuit de saisie (soumission, retour, validation…). */
@Getter
@Setter
@Entity
@Table(name = "saisie_workflow_event")
public class SaisieWorkflowEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_SAISIE_WORKFLOW_EVENT", nullable = false)
	private Integer id;

	@Column(name = "ID_SAISIE_WORKFLOW", nullable = false)
	private Integer idSaisieWorkflow;

	@Column(name = "ACTION", nullable = false, length = 40)
	private String action;

	@Column(name = "LIBELLE", length = 120)
	private String libelle;

	@Column(name = "ACTEUR", length = 100)
	private String acteur;

	@Column(name = "DATE_ETAPE")
	private Instant dateEtape;

	@Column(name = "DETAIL", length = 2000)
	private String detail;
}
