package com.dcspa.prism.service.mapper;

import com.dcspa.prism.dto.AlphaDto;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.AutoriteAutorisation;
import com.dcspa.prism.entity.Campagne;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Periodicite;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.entity.Regimealphabetisation;
import com.dcspa.prism.entity.TypeAlpha;
import com.dcspa.prism.repository.AutoriteAutorisationRepository;
import com.dcspa.prism.repository.CampagneRepository;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.PeriodiciteRepository;
import com.dcspa.prism.repository.PromoteurRepository;
import com.dcspa.prism.repository.RegimealphabetisationRepository;
import com.dcspa.prism.repository.TypeAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlphaMapper {

	private final CentreRepository centreRepository;
	private final CampagneRepository campagneRepository;
	private final TypeAlphaRepository typeAlphaRepository;
	private final RegimealphabetisationRepository regimealphabetisationRepository;
	private final PeriodiciteRepository periodiciteRepository;
	private final AutoriteAutorisationRepository autoriteAutorisationRepository;
	private final PromoteurRepository promoteurRepository;

	public AlphaDto toDto(Alpha entity) {
		if (entity == null) return null;
		return AlphaDto.builder()
				.id(entity.getId())
				.idCentre(entity.getCentre() != null ? entity.getCentre().getId() : null)
				.idCampagne(entity.getIdCompagne() != null ? entity.getIdCompagne().getId() : null)
				.idTypeAlpha(entity.getIdTypeAlpha() != null ? entity.getIdTypeAlpha().getId() : null)
				.idRegimeAlpha(entity.getIdRegimeAlpha() != null ? entity.getIdRegimeAlpha().getId() : null)
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
				.codeAlpha(entity.getCodeAlpha())
				.libelleAlpha(entity.getLibelleAlpha())
				.build();
	}

	/**
	 * Convertit un DTO en entité. Les IDs du DTO sont résolus vers les entités liées.
	 * Les relations obligatoires (centre, campagne, typeAlpha, regimeAlpha) doivent exister en base.
	 */
	public Alpha toEntity(AlphaDto dto) {
		if (dto == null) return null;

		Centre centre = dto.getIdCentre() != null
				? centreRepository.findById(dto.getIdCentre()).orElse(null)
				: null;
		Campagne campagne = dto.getIdCampagne() != null
				? campagneRepository.findById(dto.getIdCampagne()).orElse(null)
				: null;
		TypeAlpha typeAlpha = dto.getIdTypeAlpha() != null
				? typeAlphaRepository.findById(dto.getIdTypeAlpha()).orElse(null)
				: null;
		Regimealphabetisation regimeAlpha = dto.getIdRegimeAlpha() != null
				? regimealphabetisationRepository.findById(dto.getIdRegimeAlpha()).orElse(null)
				: null;
		Periodicite periodicite = dto.getIdPeriodicite() != null
				? periodiciteRepository.findById(dto.getIdPeriodicite()).orElse(null)
				: null;
		AutoriteAutorisation autorite = dto.getIdAutoriteAutorisation() != null
				? autoriteAutorisationRepository.findById(dto.getIdAutoriteAutorisation()).orElse(null)
				: null;
		Promoteur promoteur = dto.getIdPromoteur() != null
				? promoteurRepository.findById(dto.getIdPromoteur()).orElse(null)
				: null;

		Alpha entity = new Alpha();
		entity.setId(dto.getId() != null ? dto.getId() : (centre != null ? centre.getId() : null));
		entity.setCentre(centre);
		entity.setIdCompagne(campagne);
		entity.setIdTypeAlpha(typeAlpha);
		entity.setIdRegimeAlpha(regimeAlpha);
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
		entity.setCodeAlpha(dto.getCodeAlpha());
		entity.setLibelleAlpha(dto.getLibelleAlpha());
		return entity;
	}
}
