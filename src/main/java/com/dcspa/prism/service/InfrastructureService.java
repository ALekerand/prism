package com.dcspa.prism.service;

import com.dcspa.prism.entity.Infrastructure;
import com.dcspa.prism.repository.InfrastructureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InfrastructureService {

	private final InfrastructureRepository repository;

	@Transactional(readOnly = true)
	public List<Infrastructure> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Infrastructure> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public Infrastructure save(Infrastructure entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
