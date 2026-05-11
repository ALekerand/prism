package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Visite;
import com.dcspa.prism.repositorybase.BaseRepository;

public interface VisiteRepository extends BaseRepository<Visite, Integer> {
	boolean existsByIdAlpha_Id(Integer idAlpha);
}
