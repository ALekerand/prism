package com.dcspa.prism.repository;

import com.dcspa.prism.entity.LangueApprentissage;
import com.dcspa.prism.repositorybase.BaseRepository;

public interface LangueApprentissageRepository extends BaseRepository<LangueApprentissage, Integer> {

	java.util.List<LangueApprentissage> findByIdCentre_IdOrderByLibelleLangueAsc(Integer centreId);

	boolean existsByIdCentre_IdAndLibelleLangueIgnoreCase(Integer centreId, String libelleLangue);

	boolean existsByIdCentre_IdAndLibelleLangueIgnoreCaseAndIdNot(
			Integer centreId, String libelleLangue, Integer id);
}
