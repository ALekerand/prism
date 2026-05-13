package com.dcspa.prism.repository;

import com.dcspa.prism.entity.AlphaNiveau;
import com.dcspa.prism.repositorybase.BaseRepository;
import java.util.List;

public interface AlphaNiveauRepository extends BaseRepository<AlphaNiveau, Integer> {
	List<AlphaNiveau> findByIdCentre_Id(Integer centreId);

	void deleteByIdCentre_Id(Integer centreId);

	boolean existsByIdCentre_IdAndIdNiveauAlpha_Id(Integer centreId, Integer niveauAlphaId);
}
