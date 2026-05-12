package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Controle;
import com.dcspa.prism.repositorybase.BaseRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ControleRepository extends BaseRepository<Controle, Integer> {
	@EntityGraph(attributePaths = {"idAlpha", "idDiscipline", "idManuel", "idNiveauControle", "idNiveauAlpha"})
	@Query("select c from Controle c")
	List<Controle> findAllWithRefs();

	@EntityGraph(attributePaths = {"idAlpha", "idDiscipline", "idManuel", "idNiveauControle", "idNiveauAlpha"})
	@Query("select c from Controle c where c.id = :id")
	Optional<Controle> findByIdWithRefs(@Param("id") Integer id);
}
