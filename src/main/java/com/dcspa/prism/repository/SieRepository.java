package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Sie;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SieRepository extends BaseRepository<Sie, Integer>, JpaSpecificationExecutor<Sie> {

}
