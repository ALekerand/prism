package com.dcspa.prism.service;

import com.dcspa.prism.entity.RoleFonctionnalitePermission;
import com.dcspa.prism.repository.RoleFonctionnalitePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleFonctionnalitePermissionService {

	private final RoleFonctionnalitePermissionRepository repository;

	@Transactional(readOnly = true)
	public List<RoleFonctionnalitePermission> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<RoleFonctionnalitePermission> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public RoleFonctionnalitePermission save(RoleFonctionnalitePermission entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
