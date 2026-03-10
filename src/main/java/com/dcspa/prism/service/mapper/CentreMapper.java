package com.dcspa.prism.service.mapper;

import com.dcspa.prism.dto.CentreDto;
import com.dcspa.prism.entity.AutoriteAutorisation;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Periodicite;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.repository.AutoriteAutorisationRepository;
import com.dcspa.prism.repository.PeriodiciteRepository;
import com.dcspa.prism.repository.PromoteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CentreMapper {

	private final PeriodiciteRepository periodiciteRepository;
	private final AutoriteAutorisationRepository autoriteAutorisationRepository;
	private final PromoteurRepository promoteurRepository;

	public CentreDto toDto(Centre entity) {
		if (entity == null) return null;
		return CentreDto.builder()
				.id(entity.getId())
				.idPeriodicite(entity.getIdPeriodicite() != null ? entity.getIdPeriodicite().getId() : null)
				.idAutoriteAutorisation(entity.getIdAutoriteAutorisation() != null ? entity.getIdAutoriteAutorisation().getId() : null)
				.idPromoteur(entity.getIdPromoteur() != null ? entity.getIdPromoteur().getId() : null)
				.codeCentre(entity.getCodeCentre())
				.autorisation(entity.getAutorisation())
				.encadreurNonMena(entity.getEncadreurNonMena())
				.encadrerParMena(entity.getEncadrerParMena())
				.estElectrifie(entity.getEstElectrifie())
				.aDeLeau(entity.getADeLeau())
				.nombreVisite(entity.getNombreVisite())
				.build();
	}

	/**
	 * Convertit un DTO en entité. Les IDs du DTO sont résolus vers les entités liées.
	 */
	public Centre toEntity(CentreDto dto) {
		if (dto == null) return null;

		Periodicite periodicite = dto.getIdPeriodicite() != null
				? periodiciteRepository.findById(dto.getIdPeriodicite()).orElse(null)
				: null;
		AutoriteAutorisation autorite = dto.getIdAutoriteAutorisation() != null
				? autoriteAutorisationRepository.findById(dto.getIdAutoriteAutorisation()).orElse(null)
				: null;
		Promoteur promoteur = dto.getIdPromoteur() != null
				? promoteurRepository.findById(dto.getIdPromoteur()).orElse(null)
				: null;

		Centre entity = new Centre();
		if (dto.getId() != null) entity.setId(dto.getId());
		entity.setIdPeriodicite(periodicite);
		entity.setIdAutoriteAutorisation(autorite);
		entity.setIdPromoteur(promoteur);
		entity.setCodeCentre(dto.getCodeCentre());
		entity.setAutorisation(dto.getAutorisation());
		entity.setEncadreurNonMena(dto.getEncadreurNonMena());
		entity.setEncadrerParMena(dto.getEncadrerParMena());
		entity.setEstElectrifie(dto.getEstElectrifie());
		entity.setADeLeau(dto.getADeLeau());
		entity.setNombreVisite(dto.getNombreVisite());
		return entity;
	}
}
