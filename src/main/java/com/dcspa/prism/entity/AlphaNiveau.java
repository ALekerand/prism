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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
		name = "alpha_niveau",
		uniqueConstraints = @UniqueConstraint(
				name = "uk_alpha_niveau_centre_niveau",
				columnNames = { "ID_CENTRE", "ID_NIVEAU_ALPHA" }
		)
)
public class AlphaNiveau {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_ALPHA_NIVEAU", nullable = false)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_CENTRE", nullable = false)
	private Alpha idCentre;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_NIVEAU_ALPHA", nullable = false)
	private NiveauAlpha idNiveauAlpha;
}
