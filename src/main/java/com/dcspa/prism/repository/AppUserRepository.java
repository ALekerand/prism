package com.dcspa.prism.repository;

import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    boolean existsByRoles_Id(Integer roleId);

    boolean existsByUsername(String username);

    /**
     * Liste paginée administration avec filtres optionnels.
     * {@code q} : recherche insensible à la casse sur username et email.
     */
    @EntityGraph(attributePaths = { "roles" })
    @Query("""
            SELECT DISTINCT u FROM AppUser u
            LEFT JOIN u.roles r
            WHERE (:roleId IS NULL OR r.id = :roleId)
            AND (:q IS NULL
                OR LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%'))
                OR (u.email IS NOT NULL AND LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%'))))
            AND (:actif IS NULL OR u.actif = :actif)
            """)
    Page<AppUser> searchForAdmin(
            @Param("q") String q,
            @Param("roleId") Integer roleId,
            @Param("actif") Boolean actif,
            Pageable pageable);
}
