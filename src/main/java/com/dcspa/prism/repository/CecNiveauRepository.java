package com.dcspa.prism.repository;

import com.dcspa.prism.entity.CecNiveau;
import com.dcspa.prism.repositorybase.BaseRepository;

import java.util.List;

public interface CecNiveauRepository extends BaseRepository<CecNiveau, Integer> {
	List<CecNiveau> findByIdCentre_Id(Integer centreId);
}
