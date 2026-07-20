package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CentreRepository extends BaseRepository<Centre, Integer>, JpaSpecificationExecutor<Centre> {

	Optional<Centre> findByCodeCentre(String codeCentre);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update Centre c set c.actif = :actif where c.id = :id")
	int updateActifById(@Param("id") Integer id, @Param("actif") Boolean actif);
}
