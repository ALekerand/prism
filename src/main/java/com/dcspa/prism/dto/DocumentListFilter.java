package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

/** Paramètres de filtre optionnels pour la liste « visites » (documents de suivi rattachés à un Alpha). */
@Getter
@Setter
public class DocumentListFilter {
	private String q;
	private Integer id;
	private Integer idCentre;
	private Integer idNatureDocument;
	private Integer idTypeDocument;
	private String codeDocument;
}
