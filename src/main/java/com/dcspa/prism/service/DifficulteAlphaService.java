package com.dcspa.prism.service;

import com.dcspa.prism.entity.DifficulteAlpha;
import com.dcspa.prism.repository.DifficulteAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DifficulteAlphaService {

	private final DifficulteAlphaRepository difficulteAlphaRepository;

	@Transactional(readOnly = true)
	public List<DifficulteAlpha> findAll() {
		return difficulteAlphaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<DifficulteAlpha> findById(Integer id) {
		return difficulteAlphaRepository.findById(id);
	}

	@Transactional
	public DifficulteAlpha save(DifficulteAlpha entity) {
		return difficulteAlphaRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		difficulteAlphaRepository.deleteById(id);
	}
}
