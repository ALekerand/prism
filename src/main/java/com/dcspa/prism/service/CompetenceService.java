package com.dcspa.prism.service;

import com.dcspa.prism.entity.Competence;
import com.dcspa.prism.repository.CompetenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompetenceService {

	private final CompetenceRepository repository;

	@Transactional(readOnly = true)
	public List<Competence> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Competence> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public Competence save(Competence entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
