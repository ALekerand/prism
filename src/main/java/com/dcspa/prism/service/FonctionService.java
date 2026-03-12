package com.dcspa.prism.service;

import com.dcspa.prism.entity.Fonction;
import com.dcspa.prism.repository.FonctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FonctionService {

	private final FonctionRepository repository;

	@Transactional(readOnly = true)
	public List<Fonction> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Fonction> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public Fonction save(Fonction entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
