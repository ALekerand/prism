package com.dcspa.prism.repository;

import com.dcspa.prism.entity.NiveauAlpha;
import com.dcspa.prism.repositorybase.BaseRepository;
import java.util.Optional;

public interface NiveauAlphaRepository extends BaseRepository<NiveauAlpha, Integer> {

	Optional<NiveauAlpha> findByLibelleNiveauAlphaIgnoreCase(String libelleNiveauAlpha);
}
