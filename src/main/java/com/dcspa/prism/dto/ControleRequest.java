package com.dcspa.prism.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControleRequest {
	private Integer idAlpha;
	private Integer idDiscipline;
	private Integer idManuel;
	private Integer idNiveauControle;
	private LocalDate dateDemarrageAppren;
	private String jourHeureFormation;
	private Integer nombreKitManuelsSyllabaire;
	private Integer nombreKitManuelsCalculaire;
	private Integer nombreKitManuelsCvc;
	private Integer nombreKitAutre;
	private Boolean conformiteProgramme;
}
