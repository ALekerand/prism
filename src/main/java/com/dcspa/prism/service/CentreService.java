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
		if (centre.getActif() == null) {
			centre.setActif(true);
		}
		if (centre.getId() == null && centre.getDateEnregistrement() == null) {
			centre.setDateEnregistrement(java.time.LocalDate.now());
		}
		return centreRepository.save(centre);
	}

	@Transactional
	public void deleteById(Integer id) {
		centreRepository.deleteById(id);
	}

	private void validateRequiredFields(Centre centre) {
		if (centre.getIdPromoteur() == null) {
			throw new IllegalArgumentException("Le promoteur est obligatoire pour un centre.");
		}
	}
}
