package com.dcspa.prism.service;

import com.dcspa.prism.entity.DrenaDepartement;
import com.dcspa.prism.repository.DrenaDepartementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrenaDepartementService {

	private final DrenaDepartementRepository repository;

	@Transactional(readOnly = true)
	public List<DrenaDepartement> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<DrenaDepartement> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public DrenaDepartement save(DrenaDepartement entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
