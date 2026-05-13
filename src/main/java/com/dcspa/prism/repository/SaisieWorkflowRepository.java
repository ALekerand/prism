package com.dcspa.prism.repository;

import com.dcspa.prism.entity.SaisieWorkflow;
import com.dcspa.prism.repositorybase.BaseRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SaisieWorkflowRepository extends BaseRepository<SaisieWorkflow, Integer> {
	Optional<SaisieWorkflow> findByResourcePathAndRecordId(String resourcePath, Integer recordId);

	List<SaisieWorkflow> findByResourcePathAndRecordIdIn(String resourcePath, Collection<Integer> recordIds);
}
