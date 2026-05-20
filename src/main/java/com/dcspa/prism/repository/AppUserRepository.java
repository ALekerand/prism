package com.dcspa.prism.repository;

import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AppUserRepository extends BaseRepository<AppUser, Integer>, JpaSpecificationExecutor<AppUser> {

    @EntityGraph(attributePaths = {
            "roles",
            "roles.roleFonctionnalitePermissions",
            "roles.roleFonctionnalitePermissions.fonctionnalite",
            "roles.roleFonctionnalitePermissions.permission"
    })
    Optional<AppUser> findByUsername(String username);

    /**
     * Surcharge `findAll()` pour charger les rôles (évite Lazy + N+1 dans l'admin).
     * Attention : nom "findAllWithRoles" n'est pas un finder Spring Data valide sans @Query.
     */
    @Override
    @EntityGraph(attributePaths = { "roles" })
    java.util.List<AppUser> findAll();

    @Override
    @EntityGraph(attributePaths = { "roles" })
    Page<AppUser> findAll(Pageable pageable);

    @EntityGraph(attributePaths = { "roles" })
    Page<AppUser> findAll(Specification<AppUser> spec, Pageable pageable);

    boolean existsByRoles_Id(Integer roleId);

    boolean existsByUsername(String username);
}
