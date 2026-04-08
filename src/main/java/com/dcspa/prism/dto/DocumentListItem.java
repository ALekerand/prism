package com.dcspa.prism.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DocumentListItem {
	private Integer id;
	private String codeDocument;
	private String existe;
	private String ajour;
	private String bientenu;
	private String respmethode;
	private String bienrensigne;
	private Integer idCentreAlpha;
	private String codeCentre;
	private String libelleAlpha;
	private Integer idNatureDocument;
	private String libelleNatureDocument;
	private Integer idTypeDocument;
	private String codeTypeDocument;
	private String libelleTypeDocument;
}
