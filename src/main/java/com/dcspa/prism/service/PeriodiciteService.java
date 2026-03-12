package com.dcspa.prism.service;

import com.dcspa.prism.entity.Periodicite;
import com.dcspa.prism.repository.PeriodiciteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PeriodiciteService {

	private final PeriodiciteRepository repository;

	@Transactional(readOnly = true)
	public List<Periodicite> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Periodicite> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public Periodicite save(Periodicite entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
