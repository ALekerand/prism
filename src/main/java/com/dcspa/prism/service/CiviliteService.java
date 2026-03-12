package com.dcspa.prism.service;

import com.dcspa.prism.entity.Civilite;
import com.dcspa.prism.repository.CiviliteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CiviliteService {

	private final CiviliteRepository repository;

	@Transactional(readOnly = true)
	public List<Civilite> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Civilite> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public Civilite save(Civilite entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
