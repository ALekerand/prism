package com.dcspa.prism.service;

import com.dcspa.prism.entity.SourceFinancement;
import com.dcspa.prism.repository.SourceFinancementRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SourceFinancementService {

	private final SourceFinancementRepository repository;

	@Transactional(readOnly = true)
	public List<SourceFinancement> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<SourceFinancement> findById(Integer id) {
		return repository.findById(id);
	}

	@Transactional
	public SourceFinancement save(SourceFinancement entity) {
		return repository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}
}
