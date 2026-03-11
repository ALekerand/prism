package com.dcspa.prism.service;

import com.dcspa.prism.entity.ImpactAlpha;
import com.dcspa.prism.repository.ImpactAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImpactAlphaService {

	private final ImpactAlphaRepository impactAlphaRepository;

	@Transactional(readOnly = true)
	public List<ImpactAlpha> findAll() {
		return impactAlphaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<ImpactAlpha> findById(Integer id) {
		return impactAlphaRepository.findById(id);
	}

	@Transactional
	public ImpactAlpha save(ImpactAlpha entity) {
		return impactAlphaRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		impactAlphaRepository.deleteById(id);
	}
}
