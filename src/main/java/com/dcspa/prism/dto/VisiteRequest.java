package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisiteRequest {
	private String mode;
	private Integer idAlpha;
	private Integer idPeriodeActivite;
	private String maitriseSeanceLecture;
	private String maitriseSeanceEcriture;
	private String maitriseSeanceCalcul;
	private String maitriseSeanceCvc;
	private Integer nombreVisiteRealiseParConseiller;
	private Integer nombreBulletinEffectueParConseiller;
	private Integer nombreVisiteConseillerSuperviseurEffectue;
	private Integer nombreReunionBilanConseillerSuperviseur;
	private Integer nombreVisiteEffectueParIepp;
	private Integer nombreReunionPointActiviteAlpha;
}
