package com.dcspa.prism.repository;

import com.dcspa.prism.entity.ControleHoraireFormation;
import com.dcspa.prism.repositorybase.BaseRepository;
import java.util.List;

public interface ControleHoraireFormationRepository extends BaseRepository<ControleHoraireFormation, Integer> {
	List<ControleHoraireFormation> findByControle_Id(Integer idControle);
}
