package com.dcspa.prism.service;

import com.dcspa.prism.entity.Programme;
import com.dcspa.prism.repository.ProgrammeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgrammeService {

	private final ProgrammeRepository repository;

	@Transactional(readOnly = true)
	public List<Programme> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Programme> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public Programme save(Programme entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
