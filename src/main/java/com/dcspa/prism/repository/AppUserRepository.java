package com.dcspa.prism.repository;

import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface AppUserRepository extends BaseRepository<AppUser, Integer> {

    @EntityGraph(attributePaths = {
            "roles",
            "roles.roleFonctionnalitePermissions",
            "roles.roleFonctionnalitePermissions.fonctionnalite",
            "roles.roleFonctionnalitePermissions.permission"
    })
    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
