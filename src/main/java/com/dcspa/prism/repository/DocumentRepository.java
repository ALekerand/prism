package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Document;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DocumentRepository extends BaseRepository<Document, Integer>, JpaSpecificationExecutor<Document> {
}
