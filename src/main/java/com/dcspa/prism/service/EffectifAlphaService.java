package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifAlpha;
import com.dcspa.prism.repository.EffectifAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifAlphaService {

	private final EffectifAlphaRepository effectifAlphaRepository;

	@Transactional(readOnly = true)
	public List<EffectifAlpha> findAll() {
		return effectifAlphaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<EffectifAlpha> findById(Integer id) {
		return effectifAlphaRepository.findById(id);
	}

	@Transactional
	public EffectifAlpha save(EffectifAlpha entity) {
		validateRequiredFields(entity);
		return effectifAlphaRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		effectifAlphaRepository.deleteById(id);
	}

	private void validateRequiredFields(EffectifAlpha entity) {
		if (entity.getIdPeriodeActivite() == null) {
			throw new IllegalArgumentException("La période d'activité est obligatoire pour un effectif alpha.");
		}
		if (entity.getIdCentre() == null) {
			throw new IllegalArgumentException("Le centre (Alpha) est obligatoire pour un effectif alpha.");
		}
	}
}
