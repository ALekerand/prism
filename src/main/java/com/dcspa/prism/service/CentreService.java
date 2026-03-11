package com.dcspa.prism.service;

import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.repository.CentreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CentreService {

	private final CentreRepository centreRepository;

	@Transactional(readOnly = true)
	public List<Centre> findAll() {
		return centreRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Centre> findById(Integer id) {
		return centreRepository.findById(id);
	}

	@Transactional
	public Centre save(Centre centre) {
		validateRequiredFields(centre);
		return centreRepository.save(centre);
	}

	@Transactional
	public void deleteById(Integer id) {
		centreRepository.deleteById(id);
	}

	private void validateRequiredFields(Centre centre) {
		if (centre == null) {
			throw new IllegalArgumentException("L'instance Centre ne peut pas être nulle.");
		}
		if (centre.getIdPromoteur() == null) {
			throw new IllegalArgumentException("Le promoteur est obligatoire pour un centre.");
		}
		if (centre.getCodeCentre() == null || centre.getCodeCentre().isBlank()) {
			throw new IllegalArgumentException("Le code du centre est obligatoire.");
		}
	}
}
