package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Evaluation;
import com.dcspa.prism.repositorybase.BaseRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EvaluationRepository extends BaseRepository<Evaluation, Integer> {
	@Query("""
			select e from Evaluation e
			left join fetch e.idAlpha
			left join fetch e.idPeriodeEvaluation
			left join fetch e.idNiveauEvaluation
			left join fetch e.idThemeEvaluation
			left join fetch e.idTauxEvaluation
			""")
	List<Evaluation> findAllWithRefs();

	@Query("""
			select e from Evaluation e
			left join fetch e.idAlpha
			left join fetch e.idPeriodeEvaluation
			left join fetch e.idNiveauEvaluation
			left join fetch e.idThemeEvaluation
			left join fetch e.idTauxEvaluation
			where e.id = :id
			""")
	Optional<Evaluation> findByIdWithRefs(@Param("id") Integer id);
}
