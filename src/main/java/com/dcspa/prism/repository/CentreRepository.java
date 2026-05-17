package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CentreRepository extends BaseRepository<Centre, Integer>, JpaSpecificationExecutor<Centre> {

    Optional<Centre> findByCodeCentre(String codeCentre);
}
