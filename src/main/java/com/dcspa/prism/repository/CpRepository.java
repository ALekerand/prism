package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CpRepository extends BaseRepository<Cp, Integer>, JpaSpecificationExecutor<Cp> {

}
