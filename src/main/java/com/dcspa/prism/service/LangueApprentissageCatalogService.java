package com.dcspa.prism.service;

import com.dcspa.prism.config.LangueCatalogueProperties;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.LangueApprentissage;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.LangueApprentissageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LangueApprentissageCatalogService {

	private final LangueApprentissageRepository langueApprentissageRepository;
	private final CentreRepository centreRepository;
	private final LangueCatalogueProperties langueCatalogueProperties;

	@Transactional(readOnly = true)
	public List<LangueApprentissage> findCatalog() {
		return langueApprentissageRepository.findByIdCentre_IdOrderByLibelleLangueAsc(resolveCatalogueCentreId());
	}

	@Transactional(readOnly = true)
	public Optional<LangueApprentissage> findCatalogById(Integer id) {
		return langueApprentissageRepository.findById(id)
				.filter(row -> row.getIdCentre().getId().equals(resolveCatalogueCentreId()));
	}

	@Transactional
	public LangueApprentissage saveCatalog(Integer id, String libelleLangue) {
		String libelle = normalizeLibelle(libelleLangue);
		Integer centreId = resolveCatalogueCentreId();
		if (id == null) {
			if (langueApprentissageRepository.existsByIdCentre_IdAndLibelleLangueIgnoreCase(centreId, libelle)) {
				throw new IllegalArgumentException("Cette langue existe déjà dans le catalogue.");
			}
		} else if (langueApprentissageRepository.existsByIdCentre_IdAndLibelleLangueIgnoreCaseAndIdNot(
				centreId, libelle, id)) {
			throw new IllegalArgumentException("Cette langue existe déjà dans le catalogue.");
		}

		LangueApprentissage entity = id == null
				? new LangueApprentissage()
				: langueApprentissageRepository.findById(id)
						.filter(row -> row.getIdCentre().getId().equals(centreId))
						.orElseThrow(() -> new IllegalArgumentException("Langue catalogue introuvable: " + id));
		entity.setId(id);
		Centre centre = centreRepository.findById(centreId)
				.orElseThrow(() -> new IllegalArgumentException("Centre catalogue introuvable: " + centreId));
		entity.setIdCentre(centre);
		entity.setLibelleLangue(libelle);
		return langueApprentissageRepository.save(entity);
	}

	@Transactional
	public void deleteCatalogById(Integer id) {
		LangueApprentissage row = langueApprentissageRepository.findById(id)
				.filter(e -> e.getIdCentre().getId().equals(resolveCatalogueCentreId()))
				.orElseThrow(() -> new IllegalArgumentException("Langue catalogue introuvable: " + id));
		langueApprentissageRepository.delete(row);
	}

	private String normalizeLibelle(String libelleLangue) {
		if (libelleLangue == null || libelleLangue.isBlank()) {
			throw new IllegalArgumentException("libelleLangue est obligatoire.");
		}
		return libelleLangue.trim();
	}

	private Integer resolveCatalogueCentreId() {
		Integer configured = langueCatalogueProperties.getCentreId();
		if (configured != null) {
			if (centreRepository.findById(configured).isEmpty()) {
				throw new IllegalStateException(
						"prism.langue-catalogue.centre-id invalide (centre introuvable): " + configured);
			}
			return configured;
		}
		return centreRepository.findAll().stream()
				.map(Centre::getId)
				.min(Integer::compareTo)
				.orElseThrow(() -> new IllegalStateException(
						"Aucun centre disponible pour le catalogue des langues d'apprentissage."));
	}
}
