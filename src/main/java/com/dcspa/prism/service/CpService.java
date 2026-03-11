package com.dcspa.prism.service;

import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.repository.CpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CpService {

	private final CpRepository cpRepository;

	@Transactional(readOnly = true)
	public List<Cp> findAll() {
		return cpRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Cp> findById(Integer id) {
		return cpRepository.findById(id);
	}

	@Transactional
	public Cp save(Cp entity) {
		validateRequiredFields(entity);
		return cpRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		cpRepository.deleteById(id);
	}

	private void validateRequiredFields(Cp entity) {
		if (entity.getCentre() == null) {
			throw new IllegalArgumentException("Le centre est obligatoire pour un CP.");
		}
	}
}
