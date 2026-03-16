package com.dcspa.prism.repository;

import com.dcspa.prism.entity.RoleFonctionnalitePermission;
import com.dcspa.prism.repositorybase.BaseRepository;

import java.util.List;

public interface RoleFonctionnalitePermissionRepository extends BaseRepository<RoleFonctionnalitePermission, Integer> {

    List<RoleFonctionnalitePermission> findByRole_Id(Integer roleId);
}
