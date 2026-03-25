package com.dcspa.prism.service;

import com.dcspa.prism.entity.Departement;
import com.dcspa.prism.repository.DepartementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartementService {

	private final DepartementRepository repository;

	@Transactional(readOnly = true)
	public List<Departement> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Departement> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public Departement save(Departement entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
