package com.dcspa.prism.service;

import com.dcspa.prism.entity.Designation;
import com.dcspa.prism.repository.DesignationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DesignationService {

	private final DesignationRepository repository;

	@Transactional(readOnly = true)
	public List<Designation> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Designation> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public Designation save(Designation entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
