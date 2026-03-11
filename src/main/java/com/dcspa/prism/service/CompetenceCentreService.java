package com.dcspa.prism.service;

import com.dcspa.prism.entity.CompetenceCentre;
import com.dcspa.prism.repository.CompetenceCentreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompetenceCentreService {

	private final CompetenceCentreRepository competenceCentreRepository;

	@Transactional(readOnly = true)
	public List<CompetenceCentre> findAll() {
		return competenceCentreRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<CompetenceCentre> findById(Integer id) {
		return competenceCentreRepository.findById(id);
	}

	@Transactional
	public CompetenceCentre save(CompetenceCentre entity) {
		return competenceCentreRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		competenceCentreRepository.deleteById(id);
	}
}
