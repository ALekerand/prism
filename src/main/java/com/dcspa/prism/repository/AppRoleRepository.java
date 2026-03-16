package com.dcspa.prism.repository;

import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.repositorybase.BaseRepository;

import java.util.Optional;

public interface AppRoleRepository extends BaseRepository<AppRole, Integer> {

    Optional<AppRole> findByCodeRole(String codeRole);
}
