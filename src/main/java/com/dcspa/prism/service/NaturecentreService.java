package com.dcspa.prism.service;

import com.dcspa.prism.entity.Naturecentre;
import com.dcspa.prism.repository.NaturecentreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NaturecentreService {

	private final NaturecentreRepository repository;

	@Transactional(readOnly = true)
	public List<Naturecentre> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Naturecentre> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public Naturecentre save(Naturecentre entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
