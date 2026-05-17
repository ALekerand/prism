package com.dcspa.prism.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
		name = "saisie_workflow",
		uniqueConstraints = @UniqueConstraint(
				name = "uk_saisie_workflow_resource_record",
				columnNames = { "RESOURCE_PATH", "RECORD_ID" }
		)
)
public class SaisieWorkflow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_SAISIE_WORKFLOW", nullable = false)
	private Integer id;

	@Column(name = "RESOURCE_PATH", nullable = false, length = 180)
	private String resourcePath;

	@Column(name = "RECORD_ID", nullable = false)
	private Integer recordId;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUT", nullable = false, length = 40)
	private SaisieWorkflowStatus statut = SaisieWorkflowStatus.BROUILLON;

	@Column(name = "MOTIF_REJET", length = 500)
	private String motifRejet;

	@Column(name = "COMMENTAIRE_RETOUR", length = 500)
	private String commentaireRetour;

	@Column(name = "SOUMIS_PAR", length = 100)
	private String soumisPar;

	/** Conseiller (ou auteur) propriétaire de la saisie ; sert au filtrage « mes données ». */
	@Column(name = "PROPRIETAIRE", length = 100)
	private String proprietaire;

	@Column(name = "DECIDE_PAR", length = 100)
	private String decidePar;

	@Column(name = "VALIDE_COORD_PAR", length = 100)
	private String valideCoordPar;

	@Column(name = "VALIDE_SUP_PAR", length = 100)
	private String valideSupPar;

	@Column(name = "VALIDE_CENTRAL_PAR", length = 100)
	private String valideCentralPar;

	@Column(name = "DATE_SOUMISSION")
	private Instant dateSoumission;

	@Column(name = "DATE_DECISION")
	private Instant dateDecision;
}
