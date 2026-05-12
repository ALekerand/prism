package com.dcspa.prism.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControleRequest {
	private Integer idAlpha;
	private Integer idDiscipline;
	private Integer idManuel;
	private Integer idNiveauControle;
	private Integer idNiveauAlpha;
	private LocalDate dateDemarrageAppren;
	private String jourHeureFormation;
	private Integer nombreKitManuelsSyllabaire;
	private Integer nombreKitManuelsCalculaire;
	private Integer nombreKitManuelsCvc;
	private Integer nombreKitAutre;
	private Boolean conformiteProgramme;
	private List<HoraireFormationRequest> horairesFormation;
	private List<KitManuelRequest> kitsManuels;

	@Getter
	@Setter
	public static class HoraireFormationRequest {
		private String jourSemaine;
		private LocalTime heureDebut;
		private LocalTime heureFin;
	}

	@Getter
	@Setter
	public static class KitManuelRequest {
		private Integer idManuel;
		private Integer nombreKit;
		private String precisionAutre;
	}
}
