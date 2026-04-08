package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Cec;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CecRepository extends BaseRepository<Cec, Integer>, JpaSpecificationExecutor<Cec> {

}
