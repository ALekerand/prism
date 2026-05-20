package com.dcspa.prism.repository;

import com.dcspa.prism.entity.DrenaDepartement;
import com.dcspa.prism.repositorybase.BaseRepository;

public interface DrenaDepartementRepository extends BaseRepository<DrenaDepartement, Integer> {

	boolean existsByIdDrena_IdAndIdDepartement_Id(Integer idDrena, Integer idDepartement);

	boolean existsByIdDrena_IdAndIdDepartement_IdRegion_Id(Integer idDrena, Integer idRegion);
}
