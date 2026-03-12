package com.dcspa.prism.service;

import com.dcspa.prism.entity.Modealphabetisation;
import com.dcspa.prism.repository.ModealphabetisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModealphabetisationService {

	private final ModealphabetisationRepository repository;

	@Transactional(readOnly = true)
	public List<Modealphabetisation> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Modealphabetisation> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public Modealphabetisation save(Modealphabetisation entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
