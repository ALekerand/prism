package com.dcspa.prism.entity;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@Entity
@Table(name = "controle")
public class Controle implements ActivitesCentreWorkflowEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_CONTROLE", nullable = false)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_ALPHA", nullable = false)
	private Alpha idAlpha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DISCIPLINE")
	private Discipline idDiscipline;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MANUEL")
	private Manuel idManuel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_NIVEAU_CONTROLE")
	private NiveauControle idNiveauControle;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_NIVEAU_ALPHA")
	private NiveauAlpha idNiveauAlpha;

	@Column(name = "DATE_DEMARRAGE_APPREN")
	private LocalDate dateDemarrageAppren;

	@Column(name = "JOUR_HEURE_FORMATION", length = 100)
	private String jourHeureFormation;

	@Column(name = "NOMBRE_KIT_MANUELS_SYLLABAIRE")
	private Integer nombreKitManuelsSyllabaire;

	@Column(name = "NOMBRE_KIT_MANUELS_CALCULAIRE")
	private Integer nombreKitManuelsCalculaire;

	@Column(name = "NOMBRE_KIT_MANUELS_CVC")
	private Integer nombreKitManuelsCvc;

	@Column(name = "NOMBRE_KIT_AUTRE")
	private Integer nombreKitAutre;

	@Column(name = "CONFORMITE_PROGRAMME")
	private Boolean conformiteProgramme;

	@Column(name = "VALIDEE_COORDONNATEUR")
	private Boolean valideeCoordonnateur;

	@Column(name = "VALIDEE_SUPERVISEUR")
	private Boolean valideeSuperviseur;

	@Column(name = "VALIDEE_CENTRALE")
	private Boolean valideeCentrale;

	@OneToMany(mappedBy = "controle", cascade = CascadeType.ALL, orphanRemoval = true)
	@BatchSize(size = 50)
	private List<ControleHoraireFormation> horairesFormation = new ArrayList<>();

	@OneToMany(mappedBy = "controle", cascade = CascadeType.ALL, orphanRemoval = true)
	@BatchSize(size = 50)
	private List<ControleKitManuel> kitsManuels = new ArrayList<>();
}
