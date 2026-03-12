package com.dcspa.prism.service;

import com.dcspa.prism.entity.DiplomePersonnel;
import com.dcspa.prism.repository.DiplomePersonnelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiplomePersonnelService {

	private final DiplomePersonnelRepository repository;

	@Transactional(readOnly = true)
	public List<DiplomePersonnel> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<DiplomePersonnel> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public DiplomePersonnel save(DiplomePersonnel entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
