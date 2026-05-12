package com.dcspa.prism.repository;

import com.dcspa.prism.entity.CpNiveau;
import com.dcspa.prism.repositorybase.BaseRepository;

import java.util.List;

public interface CpNiveauRepository extends BaseRepository<CpNiveau, Integer> {
	List<CpNiveau> findByIdCentre_Id(Integer centreId);
}
