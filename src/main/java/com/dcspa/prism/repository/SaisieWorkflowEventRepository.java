package com.dcspa.prism.repository;

import com.dcspa.prism.entity.SaisieWorkflowEvent;
import com.dcspa.prism.repositorybase.BaseRepository;
import java.util.List;

public interface SaisieWorkflowEventRepository extends BaseRepository<SaisieWorkflowEvent, Integer> {
	List<SaisieWorkflowEvent> findByIdSaisieWorkflowOrderByIdAsc(Integer idSaisieWorkflow);
}
