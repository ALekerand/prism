package com.dcspa.prism.controller.support;

import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.repository.AlphaRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** idIep / idDrena pour {@link com.dcspa.prism.service.SaisieWorkflowVisibilityRules} sans fetch eager fragile. */
@Component
@RequiredArgsConstructor
public class ActivitesCentreCirconscriptionEnricher {

	private final AlphaRepository alphaRepository;

	public void putAlphaCirconscription(Map<String, Object> row, Alpha alpha) {
		if (row == null) {
			return;
		}
		Integer alphaId = JpaAssociationIds.intIdOrNull(alpha);
		if (alphaId == null) {
			return;
		}
		alphaRepository.findCirconscriptionScopeByAlphaId(alphaId).ifPresent(scope -> {
			row.put("idIep", scope.idIep());
			if (scope.idDrena() != null) {
				row.put("idDrena", scope.idDrena());
			}
		});
	}
}
