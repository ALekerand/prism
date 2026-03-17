package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Fonctionnalite;
import com.dcspa.prism.repositorybase.BaseRepository;

import java.util.Optional;

public interface FonctionnaliteRepository extends BaseRepository<Fonctionnalite, Integer> {

    Optional<Fonctionnalite> findByCodeFonctionnalite(String codeFonctionnalite);
}
