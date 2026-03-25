package com.dcspa.prism.service;

import com.dcspa.prism.entity.Fonctionnalite;
import com.dcspa.prism.repository.FonctionnaliteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FonctionnaliteService {

	private final FonctionnaliteRepository repository;

	@Transactional(readOnly = true)
	public List<Fonctionnalite> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Fonctionnalite> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public Fonctionnalite save(Fonctionnalite entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
