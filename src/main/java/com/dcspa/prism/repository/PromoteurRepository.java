package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PromoteurRepository extends BaseRepository<Promoteur, Integer>, JpaSpecificationExecutor<Promoteur> {

}
