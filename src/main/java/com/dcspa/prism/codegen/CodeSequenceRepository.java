package com.dcspa.prism.codegen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface CodeSequenceRepository extends JpaRepository<CodeSequence, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select cs from CodeSequence cs where cs.prefix = :prefix")
    Optional<CodeSequence> lockByPrefix(@Param("prefix") String prefix);
}

