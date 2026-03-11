package com.dcspa.prism.service;

import com.dcspa.prism.entity.CpNiveau;
import com.dcspa.prism.repository.CpNiveauRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CpNiveauService {

	private final CpNiveauRepository cpNiveauRepository;

	@Transactional(readOnly = true)
	public List<CpNiveau> findAll() {
		return cpNiveauRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<CpNiveau> findById(Integer id) {
		return cpNiveauRepository.findById(id);
	}

	@Transactional
	public CpNiveau save(CpNiveau entity) {
		return cpNiveauRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		cpNiveauRepository.deleteById(id);
	}
}
