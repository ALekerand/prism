package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppuiPartenaireRequest {
	private Integer idCategorieAppui;
	private Integer idCentre;
	private Integer idPartenaire;
	private String libelleAppuiPartenaire;
}
