package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlphaRepository extends BaseRepository<Alpha, Integer>, JpaSpecificationExecutor<Alpha> {
}
