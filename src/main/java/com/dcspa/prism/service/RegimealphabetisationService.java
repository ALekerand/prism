package com.dcspa.prism.service;

import com.dcspa.prism.entity.Regimealphabetisation;
import com.dcspa.prism.repository.RegimealphabetisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegimealphabetisationService {

	private final RegimealphabetisationRepository repository;

	@Transactional(readOnly = true)
	public List<Regimealphabetisation> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Regimealphabetisation> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public Regimealphabetisation save(Regimealphabetisation entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
