package com.dcspa.prism.service.circonscription;

/**
 * Périmètre effectif pour filtrer les entités rattachées à un centre (fiche Alpha, CEC, CP, SIE).
 *
 * @param level niveau de restriction
 * @param scopeId identifiant métier associé (id IEP, id DRENA ou id région selon le niveau)
 */
public record CirconscriptionAttachement(CirconscriptionLevel level, Integer scopeId) {

	public static CirconscriptionAttachement none() {
		return new CirconscriptionAttachement(CirconscriptionLevel.NONE, null);
	}

	public static CirconscriptionAttachement iep(Integer idIep) {
		return new CirconscriptionAttachement(CirconscriptionLevel.IEP, idIep);
	}

	public static CirconscriptionAttachement drena(Integer idDrena) {
		return new CirconscriptionAttachement(CirconscriptionLevel.DRENA, idDrena);
	}

	public static CirconscriptionAttachement region(Integer idRegion) {
		return new CirconscriptionAttachement(CirconscriptionLevel.REGION, idRegion);
	}
}
