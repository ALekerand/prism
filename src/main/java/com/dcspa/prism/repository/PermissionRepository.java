package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Permission;
import com.dcspa.prism.repositorybase.BaseRepository;

import java.util.Optional;

public interface PermissionRepository extends BaseRepository<Permission, Integer> {

    Optional<Permission> findByCodePermission(String codePermission);
}
