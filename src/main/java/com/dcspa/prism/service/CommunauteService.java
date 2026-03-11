package com.dcspa.prism.service;

import com.dcspa.prism.entity.Communaute;
import com.dcspa.prism.repository.CommunauteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunauteService {

	private final CommunauteRepository communauteRepository;

	@Transactional(readOnly = true)
	public List<Communaute> findAll() {
		return communauteRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Communaute> findById(Integer id) {
		return communauteRepository.findById(id);
	}

	@Transactional
	public Communaute save(Communaute entity) {
		validateRequiredFields(entity);
		return communauteRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		communauteRepository.deleteById(id);
	}

	private void validateRequiredFields(Communaute entity) {
		if (entity.getPersonnemorale() == null) {
			throw new IllegalArgumentException("La personne morale (promoteur) est obligatoire pour une communauté.");
		}
	}
}
