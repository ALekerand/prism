package com.dcspa.prism.service;

import com.dcspa.prism.entity.NiveauCp;
import com.dcspa.prism.repository.NiveauCpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NiveauCpService {

	private final NiveauCpRepository repository;

	@Transactional(readOnly = true)
	public List<NiveauCp> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<NiveauCp> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public NiveauCp save(NiveauCp entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
