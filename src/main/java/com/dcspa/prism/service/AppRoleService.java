package com.dcspa.prism.service;

import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.repository.RoleFonctionnalitePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppRoleService {

	private final AppRoleRepository repository;
	private final AppUserRepository appUserRepository;
	private final RoleFonctionnalitePermissionRepository roleFonctionnalitePermissionRepository;

	@Transactional(readOnly = true)
	public List<AppRole> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<AppRole> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public AppRole save(AppRole entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) {
		if (id == null) return;

		if (appUserRepository.existsByRoles_Id(id)) {
			throw new IllegalArgumentException("Suppression impossible : ce rôle est déjà attribué à un ou plusieurs utilisateurs.");
		}
		if (roleFonctionnalitePermissionRepository.existsByRole_Id(id)) {
			throw new IllegalArgumentException("Suppression impossible : ce rôle possède déjà des droits (fonctionnalités / permissions).");
		}

		repository.deleteById(id);
	}
}
