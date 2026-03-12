package com.dcspa.prism.service;

import com.dcspa.prism.entity.Particulier;
import com.dcspa.prism.repository.ParticulierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticulierService {

	private final ParticulierRepository repository;

	@Transactional(readOnly = true)
	public List<Particulier> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Particulier> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public Particulier save(Particulier entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
