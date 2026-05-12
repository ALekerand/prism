package com.dcspa.prism.repository;

import com.dcspa.prism.entity.EvaluationThemeTaux;
import com.dcspa.prism.repositorybase.BaseRepository;
import java.util.List;

public interface EvaluationThemeTauxRepository extends BaseRepository<EvaluationThemeTaux, Integer> {
	List<EvaluationThemeTaux> findByEvaluation_Id(Integer idEvaluation);
}
