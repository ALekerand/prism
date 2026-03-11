package com.dcspa.prism.service;

import com.dcspa.prism.entity.LangueApprentissage;
import com.dcspa.prism.repository.LangueApprentissageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LangueApprentissageService {

	private final LangueApprentissageRepository langueApprentissageRepository;

	@Transactional(readOnly = true)
	public List<LangueApprentissage> findAll() {
		return langueApprentissageRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<LangueApprentissage> findById(Integer id) {
		return langueApprentissageRepository.findById(id);
	}

	@Transactional
	public LangueApprentissage save(LangueApprentissage entity) {
		return langueApprentissageRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		langueApprentissageRepository.deleteById(id);
	}
}
