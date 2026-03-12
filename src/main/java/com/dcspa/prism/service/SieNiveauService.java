package com.dcspa.prism.service;

import com.dcspa.prism.entity.SieNiveau;
import com.dcspa.prism.repository.SieNiveauRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SieNiveauService {

	private final SieNiveauRepository repository;

	@Transactional(readOnly = true)
	public List<SieNiveau> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<SieNiveau> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public SieNiveau save(SieNiveau entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
