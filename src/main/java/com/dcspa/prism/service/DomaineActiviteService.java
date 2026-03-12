package com.dcspa.prism.service;

import com.dcspa.prism.entity.DomaineActivite;
import com.dcspa.prism.repository.DomaineActiviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DomaineActiviteService {

	private final DomaineActiviteRepository repository;

	@Transactional(readOnly = true)
	public List<DomaineActivite> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<DomaineActivite> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public DomaineActivite save(DomaineActivite entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
