package com.dcspa.prism.service;

import com.dcspa.prism.entity.Drena;
import com.dcspa.prism.repository.DrenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrenaService {

	private final DrenaRepository repository;

	@Transactional(readOnly = true)
	public List<Drena> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Drena> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public Drena save(Drena entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
