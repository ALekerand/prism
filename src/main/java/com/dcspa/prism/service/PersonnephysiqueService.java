package com.dcspa.prism.service;

import com.dcspa.prism.entity.Personnephysique;
import com.dcspa.prism.repository.PersonnephysiqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonnephysiqueService {

	private final PersonnephysiqueRepository repository;

	@Transactional(readOnly = true)
	public List<Personnephysique> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Personnephysique> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public Personnephysique save(Personnephysique entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
