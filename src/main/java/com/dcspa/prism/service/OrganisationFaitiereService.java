package com.dcspa.prism.service;

import com.dcspa.prism.entity.OrganisationFaitiere;
import com.dcspa.prism.repository.OrganisationFaitiereRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganisationFaitiereService {

	private final OrganisationFaitiereRepository repository;

	@Transactional(readOnly = true)
	public List<OrganisationFaitiere> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<OrganisationFaitiere> findById(Integer id) {
		return repository.findById(id);
	}

	@Transactional
	public OrganisationFaitiere save(OrganisationFaitiere entity) {
		return repository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}
}
