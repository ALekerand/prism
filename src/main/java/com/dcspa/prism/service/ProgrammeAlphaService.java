package com.dcspa.prism.service;

import com.dcspa.prism.entity.ProgrammeAlpha;
import com.dcspa.prism.repository.ProgrammeAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgrammeAlphaService {

	private final ProgrammeAlphaRepository repository;

	@Transactional(readOnly = true)
	public List<ProgrammeAlpha> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<ProgrammeAlpha> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public ProgrammeAlpha save(ProgrammeAlpha entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
