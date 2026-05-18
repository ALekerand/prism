package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Evaluation;
import com.dcspa.prism.repositorybase.BaseRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EvaluationRepository extends BaseRepository<Evaluation, Integer>, JpaSpecificationExecutor<Evaluation> {
	@Query("""
			select distinct e from Evaluation e
			left join fetch e.idAlpha
			left join fetch e.idPeriodeActivite
			left join fetch e.idNiveauEvaluation
			left join fetch e.idThemeEvaluation
			left join fetch e.idTauxEvaluation
			left join fetch e.themesTaux tt
			left join fetch tt.themeEvaluation
			""")
	List<Evaluation> findAllWithRefs();

	@Query("""
			select distinct e from Evaluation e
			left join fetch e.idAlpha
			left join fetch e.idPeriodeActivite
			left join fetch e.idNiveauEvaluation
			left join fetch e.idThemeEvaluation
			left join fetch e.idTauxEvaluation
			left join fetch e.themesTaux tt
			left join fetch tt.themeEvaluation
			where e.id = :id
			""")
	Optional<Evaluation> findByIdWithRefs(@Param("id") Integer id);
}
