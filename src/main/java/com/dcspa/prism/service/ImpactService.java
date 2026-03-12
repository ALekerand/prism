package com.dcspa.prism.service;

import com.dcspa.prism.entity.Impact;
import com.dcspa.prism.repository.ImpactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImpactService {

	private final ImpactRepository repository;

	@Transactional(readOnly = true)
	public List<Impact> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Impact> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public Impact save(Impact entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
