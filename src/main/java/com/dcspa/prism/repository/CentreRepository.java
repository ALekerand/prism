package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.repositorybase.BaseRepository;

import java.util.Optional;

public interface CentreRepository extends BaseRepository<Centre, Integer> {

    Optional<Centre> findByCodeCentre(String codeCentre);
}
