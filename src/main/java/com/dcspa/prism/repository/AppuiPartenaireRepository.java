package com.dcspa.prism.repository;

import com.dcspa.prism.entity.AppuiPartenaire;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AppuiPartenaireRepository extends BaseRepository<AppuiPartenaire, Integer> {

	@Query(
			"""
			SELECT DISTINCT a FROM AppuiPartenaire a
			JOIN FETCH a.idCategorieAppui
			JOIN FETCH a.idCentre
			JOIN FETCH a.idPartenaire
			""")
	List<AppuiPartenaire> findAllWithAssociations();

	@Query(
			"""
			SELECT a FROM AppuiPartenaire a
			JOIN FETCH a.idCategorieAppui
			JOIN FETCH a.idCentre
			JOIN FETCH a.idPartenaire
			WHERE a.id = :id
			""")
	Optional<AppuiPartenaire> findByIdWithAssociations(@Param("id") Integer id);
}
