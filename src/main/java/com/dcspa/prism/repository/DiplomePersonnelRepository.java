package com.dcspa.prism.repository;

import com.dcspa.prism.entity.DiplomePersonnel;
import com.dcspa.prism.repositorybase.BaseRepository;

public interface DiplomePersonnelRepository extends BaseRepository<DiplomePersonnel, Integer> {
	java.util.List<DiplomePersonnel> findByIdPersonnel_Id(Integer personnelId);

	void deleteByIdPersonnel_Id(Integer personnelId);
}
