package com.dcspa.prism.service;

import com.dcspa.prism.entity.Commune;
import com.dcspa.prism.repository.CommuneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommuneService {

	private final CommuneRepository repository;

	@Transactional(readOnly = true)
	public List<Commune> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Commune> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public Commune save(Commune entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
