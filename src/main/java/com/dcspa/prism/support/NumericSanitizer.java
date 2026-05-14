package com.dcspa.prism.support;

/**
 * Normalisation des entiers métier (effectifs, visites, etc.).
 */
public final class NumericSanitizer {

	private NumericSanitizer() {
	}

	/** {@code null} conservé ; valeurs strictement négatives → {@code null}. */
	public static Integer nonNegativeOrNull(Integer v) {
		if (v == null) {
			return null;
		}
		return v < 0 ? null : v;
	}

	/**
	 * Total apprenants dérivé des effectifs H/F : si les deux sont absents, {@code null} ;
	 * sinon somme en traitant les absents comme 0.
	 */
	public static Integer totalApprenantsFromGenres(Integer totalHommes, Integer totalFemmes) {
		Integer h = nonNegativeOrNull(totalHommes);
		Integer f = nonNegativeOrNull(totalFemmes);
		if (h == null && f == null) {
			return null;
		}
		return (h == null ? 0 : h) + (f == null ? 0 : f);
	}
}
