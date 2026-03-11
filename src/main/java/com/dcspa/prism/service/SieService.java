package com.dcspa.prism.service;

import com.dcspa.prism.entity.Sie;
import com.dcspa.prism.repository.SieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SieService {

	private final SieRepository sieRepository;

	@Transactional(readOnly = true)
	public List<Sie> findAll() {
		return sieRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Sie> findById(Integer id) {
		return sieRepository.findById(id);
	}

	@Transactional
	public Sie save(Sie entity) {
		validateRequiredFields(entity);
		return sieRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		sieRepository.deleteById(id);
	}

	private void validateRequiredFields(Sie entity) {
		if (entity.getCentre() == null) {
			throw new IllegalArgumentException("Le centre est obligatoire pour un SIE.");
		}
	}
}
