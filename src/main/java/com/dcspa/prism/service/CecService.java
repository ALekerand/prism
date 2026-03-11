package com.dcspa.prism.service;

import com.dcspa.prism.entity.Cec;
import com.dcspa.prism.repository.CecRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CecService {

	private final CecRepository cecRepository;

	@Transactional(readOnly = true)
	public List<Cec> findAll() {
		return cecRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Cec> findById(Integer id) {
		return cecRepository.findById(id);
	}

	@Transactional
	public Cec save(Cec entity) {
		validateRequiredFields(entity);
		return cecRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		cecRepository.deleteById(id);
	}

	private void validateRequiredFields(Cec entity) {
		if (entity.getCentre() == null) {
			throw new IllegalArgumentException("Le centre est obligatoire pour un CEC.");
		}
	}
}
