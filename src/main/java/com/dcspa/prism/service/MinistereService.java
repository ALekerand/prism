package com.dcspa.prism.service;

import com.dcspa.prism.entity.Ministere;
import com.dcspa.prism.repository.MinistereRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MinistereService {

	private final MinistereRepository repository;

	@Transactional(readOnly = true)
	public List<Ministere> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Ministere> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public Ministere save(Ministere entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
