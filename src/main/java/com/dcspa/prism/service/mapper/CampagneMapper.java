package com.dcspa.prism.service.mapper;

import com.dcspa.prism.dto.CampagneDto;
import com.dcspa.prism.entity.Campagne;
import org.springframework.stereotype.Component;

@Component
public class CampagneMapper {

	public CampagneDto toDto(Campagne entity) {
		if (entity == null) return null;
		return CampagneDto.builder()
				.id(entity.getId())
				.codeCampagne(entity.getCodeCampagne())
				.dateDebutCampagne(entity.getDateDebutCampagne())
				.dateFinCampagne(entity.getDateFinCampagne())
				.etatCampagne(entity.getEtatCampagne())
				.build();
	}
}
