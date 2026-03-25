package com.dcspa.prism.service;

import com.dcspa.prism.entity.LocaliteDImplantation;
import com.dcspa.prism.repository.LocaliteDImplantationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocaliteDImplantationService {

	private final LocaliteDImplantationRepository repository;

	@Transactional(readOnly = true)
	public List<LocaliteDImplantation> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<LocaliteDImplantation> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public LocaliteDImplantation save(LocaliteDImplantation entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
