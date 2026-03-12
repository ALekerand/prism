package com.dcspa.prism.service;

import com.dcspa.prism.entity.Diplome;
import com.dcspa.prism.repository.DiplomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiplomeService {

	private final DiplomeRepository repository;

	@Transactional(readOnly = true)
	public List<Diplome> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Diplome> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public Diplome save(Diplome entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
