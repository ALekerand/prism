package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Visite;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VisiteRepository extends BaseRepository<Visite, Integer>, JpaSpecificationExecutor<Visite> {
	boolean existsByIdAlpha_Id(Integer idAlpha);
	long countByIdAlpha_Id(Integer idAlpha);
}
