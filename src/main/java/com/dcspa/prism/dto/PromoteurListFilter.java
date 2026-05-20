package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromoteurListFilter {
	private String q;
	private String typePromoteur;
	private String codePromoteur;
	private String libellePromoteur;
}
