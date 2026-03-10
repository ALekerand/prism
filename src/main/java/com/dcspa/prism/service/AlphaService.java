package com.dcspa.prism.service;

import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.repository.AlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlphaService {

	private final AlphaRepository alphaRepository;

	@Transactional(readOnly = true)
	public List<Alpha> findAll() {
		return alphaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Alpha> findById(Integer id) {
		return alphaRepository.findById(id);
	}

	@Transactional
	public Alpha save(Alpha alpha) {
		validateRequiredFields(alpha);
		return alphaRepository.save(alpha);
	}

	@Transactional
	public void deleteById(Integer id) {
		alphaRepository.deleteById(id);
	}

	private void validateRequiredFields(Alpha alpha) {
		if (alpha == null) {
			throw new IllegalArgumentException("L'instance Alpha ne peut pas être nulle.");
		}
		if (alpha.getCentre() == null) {
			throw new IllegalArgumentException("Le centre est obligatoire pour une Alpha.");
		}
		if (alpha.getIdCompagne() == null) {
			throw new IllegalArgumentException("La campagne est obligatoire pour une Alpha.");
		}
		if (alpha.getIdTypeAlpha() == null) {
			throw new IllegalArgumentException("Le type d'alpha est obligatoire.");
		}
		if (alpha.getIdRegimeAlpha() == null) {
			throw new IllegalArgumentException("Le régime d'alphabétisation est obligatoire.");
		}
	}
}
