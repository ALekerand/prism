package com.dcspa.prism.service;

import com.dcspa.prism.entity.Permission;
import com.dcspa.prism.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionService {

	private final PermissionRepository repository;

	@Transactional(readOnly = true)
	public List<Permission> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Permission> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public Permission save(Permission entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
