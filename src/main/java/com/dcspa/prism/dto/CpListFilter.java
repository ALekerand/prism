package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CpListFilter extends SimpleCentreListFilterBase {
	private String libellleCp;
	/** Filtre explicite : true = centres promus uniquement, false = non promus. */
	private Boolean estPromu;
	/** Si true (défaut côté service), exclut les centres promus de la liste standard. */
	private Boolean excludePromu;
}
