package com.dcspa.prism.codegen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CodeSequenceRepository extends JpaRepository<CodeSequence, String> {

    /**
     * Verrouillage de ligne compatible MySQL / MariaDB.
     * Le verrou pessimiste JPQL ({@code @Lock}) fait générer par Hibernate 7 une clause
     * {@code FOR UPDATE OF alias} (PostgreSQL), refusée par MariaDB.
     */
    @Query(
            value = "SELECT * FROM code_sequence WHERE PREFIX = :prefix FOR UPDATE",
            nativeQuery = true)
    Optional<CodeSequence> lockByPrefix(@Param("prefix") String prefix);
}
