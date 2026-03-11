package com.dcspa.prism.service;

import com.dcspa.prism.entity.CecNiveau;
import com.dcspa.prism.repository.CecNiveauRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CecNiveauService {

	private final CecNiveauRepository cecNiveauRepository;

	@Transactional(readOnly = true)
	public List<CecNiveau> findAll() {
		return cecNiveauRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<CecNiveau> findById(Integer id) {
		return cecNiveauRepository.findById(id);
	}

	@Transactional
	public CecNiveau save(CecNiveau entity) {
		return cecNiveauRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		cecNiveauRepository.deleteById(id);
	}
}
