package com.dcspa.prism.service;

import com.dcspa.prism.entity.AnneScolaire;
import com.dcspa.prism.repository.AnneScolaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnneScolaireService {

	private final AnneScolaireRepository repository;

	@Transactional(readOnly = true)
	public List<AnneScolaire> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<AnneScolaire> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public AnneScolaire save(AnneScolaire entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
