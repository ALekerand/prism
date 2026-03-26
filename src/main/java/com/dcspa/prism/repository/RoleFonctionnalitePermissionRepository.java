package com.dcspa.prism.repository;

import com.dcspa.prism.entity.RoleFonctionnalitePermission;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface RoleFonctionnalitePermissionRepository extends BaseRepository<RoleFonctionnalitePermission, Integer> {

    @Override
    @EntityGraph(attributePaths = { "role", "fonctionnalite", "permission" })
    List<RoleFonctionnalitePermission> findAll();

    @EntityGraph(attributePaths = { "role", "fonctionnalite", "permission" })
    List<RoleFonctionnalitePermission> findByRole_Id(Integer roleId);

    boolean existsByRole_Id(Integer roleId);

    boolean existsByRole_IdAndFonctionnalite_IdAndPermission_Id(Integer roleId, Integer fonctionnaliteId, Integer permissionId);
}
